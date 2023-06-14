package com.sharestudio.analytics.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharestudio.analytics.dto.*;
import com.sharestudio.analytics.entity.*;
import com.sharestudio.analytics.repository.*;
import com.sharestudio.analytics.utils.StaticValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService{
    private final StatRepository statRepository;
    private final QandaRepository qandaRepository;
    private final BrandingRepository brandingRepository;
    private final HomepageRepository homepageRepository;
    private final ViewerRepository viewerRepository;
    private final VisitRepository visitRepository;
    private final MongoTemplate mongoTemplate;

    ObjectMapper objectMapper = new ObjectMapper();;

    //USER ACCOUNT TYPE REPORT WITH FILTER BY ORGANIZATIONS ID
    public ResponseEntity getUserAccountTypeReport(List<String> organizationList, List<String> clientList, List<String> portalList, List<String> dateRange) {
        List<Stat> statList = null;
        List<EventTypes> eventList = List.of(EventTypes.INVITEE_REGISTRATION_SUCCESS,EventTypes.LOGIN_SUCCESS,EventTypes.REGISTRATION_SUCCESS);
        List<UserAccountTypeReportDto> userAccountTypeReportDtoList = new ArrayList<>();
        Map<String, UserAccountTypeReportDto> valueMap = new HashMap<>();
        List<HashMap<String,String>> mapList = new ArrayList<>();
        try{
            statList = this.getStatDataAfterFilter(eventList,organizationList,clientList,portalList,dateRange);
            if(statList != null) {
                for (Stat stat : statList) {
                    Object userInfo = stat.getUserInfo();
                    String jsonObj = objectMapper.writeValueAsString(userInfo);
                    JsonNode nodeUserInfo = objectMapper.readTree(jsonObj);
                    String email = nodeUserInfo.path(StaticValues.email).asText();
                    String optionalFields = nodeUserInfo.path(StaticValues.optionalFields).asText();
                    EventTypes buttonEvent=stat.getButtonEvent();

                    if (valueMap.containsKey(email)) {
                        UserAccountTypeReportDto userAccountTypeReportDto = valueMap.get(nodeUserInfo.path(StaticValues.email).asText());
                        optionalFieldCapture(valueMap, nodeUserInfo, email, optionalFields, buttonEvent, userAccountTypeReportDto);
                    } else {
                        UserAccountTypeReportDto userAccountTypeReportDto = new UserAccountTypeReportDto();
                        userAccountTypeReportDto.setFirstName(nodeUserInfo.path(StaticValues.firstName).asText());
                        userAccountTypeReportDto.setLastName(nodeUserInfo.path(StaticValues.lastName).asText());
                        userAccountTypeReportDto.setEmail(nodeUserInfo.path(StaticValues.email).asText());
                        optionalFieldCapture(valueMap, nodeUserInfo, email, optionalFields, buttonEvent, userAccountTypeReportDto);
                    }
                }
            }
            for(UserAccountTypeReportDto value:valueMap.values()){
                HashMap<String ,String> tempMap = new HashMap<>();
                tempMap.put(StaticValues.firstName,value.getFirstName());
                tempMap.put(StaticValues.lastName,value.getLastName());
                tempMap.put(StaticValues.email,value.getEmail());
                tempMap.put(StaticValues.invited,value.getInvited());
                tempMap.put(StaticValues.registered,value.getRegistered());
                tempMap.put(StaticValues.attended,value.getAttended());
                tempMap.put(StaticValues.created,value.getCreatedAt());
                if(!value.getOptionalData().isEmpty()){
                    for (Map.Entry entry : value.getOptionalData().entrySet()){
                        tempMap.put(entry.getKey().toString(),entry.getValue().toString());
                    }
                }
                mapList.add(tempMap);
//                userAccountTypeReportDtoList.add(value);
            }
        }catch (ParseException e){
            log.error(e.getMessage());
        }catch (JsonProcessingException e){
            log.error(e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(mapList);
    }

    private void optionalFieldCapture(Map<String, UserAccountTypeReportDto> valueMap, JsonNode nodeUserInfo, String email, String optionalFields, EventTypes buttonEvent, UserAccountTypeReportDto userAccountTypeReportDto) {
        HashMap<String,String> tempValueMap = new HashMap<>();
        if(optionalFields != null && optionalFields.length() > 3){
            String s = optionalFields;
            s=s.replace(" ","");
            s=s.substring(1,s.length()-1);
            String [] toupleArray = s.split(",");
            for (int i=0;i<toupleArray.length;i++){
                if(toupleArray[i].split("=").length > 1){
                    tempValueMap.put(toupleArray[i].split("=")[0],toupleArray[i].split("=")[1]);
                }else {
                    tempValueMap.put(toupleArray[i].split("=")[0],"null");
                }
            }
            userAccountTypeReportDto.setOptionalData(tempValueMap);
        }
        switch (buttonEvent){
            case LOGIN_SUCCESS: {
                userAccountTypeReportDto.setAttended("Yes");
                break;
            }
            case REGISTRATION_SUCCESS:{
                if(nodeUserInfo.path(StaticValues.created).asText() != null){
                    userAccountTypeReportDto.setCreatedAt(nodeUserInfo.path(StaticValues.created).asText());
                }
                userAccountTypeReportDto.setRegistered("Yes");
                break;
            }
            case INVITEE_REGISTRATION_SUCCESS:{
                if(nodeUserInfo.path(StaticValues.created).asText() != null){
                    userAccountTypeReportDto.setCreatedAt(nodeUserInfo.path(StaticValues.created).asText());
                }
                userAccountTypeReportDto.setInvited("Yes");
                break;
            }
        }
        valueMap.put(email, userAccountTypeReportDto);
    }


    //WEBCAST TILE REPORT
    public  List<UserAccountLiveDataDto> getUserInfoForWebcastViewReport(List<String> organizationList,List<String> clientList,List<String> portalList,List<String> dateRange) {
        List<EventTypes> eventList = List.of(EventTypes.WATCH_LIVE);
        Map<Pair<String,String>,UserAccountLiveDataDto> userAccountLiveDataDtoMap = new HashMap<>();
        List<UserAccountLiveDataDto> userAccountLiveDataDtoList = new ArrayList<>();
        try {
            List<Stat> liveData = this.getStatDataAfterFilter(eventList,organizationList,clientList,portalList,dateRange);
            List<Integer> webcastIdList = new ArrayList<>();
            if(liveData!=null) {
                for (Stat stat : liveData) {
                    Object userInfo = stat.getUserInfo();
                    Object webCast = stat.getWebcast();

                    String userInfoString = objectMapper.writeValueAsString(userInfo);
                    String webCastString = objectMapper.writeValueAsString(webCast);

                    JsonNode userInfoNode = objectMapper.readTree(userInfoString);
                    JsonNode webCastNode = objectMapper.readTree(webCastString);
                    String legacyStatus = webCastNode.path("legacyStatus").asText();
                    if (userInfo != null && !userAccountLiveDataDtoMap.containsKey(Pair.of(userInfoNode.path(StaticValues.email).asText(),webCastNode.path(StaticValues.webcastId).asText())) &&
                            (legacyStatus.equals(StaticValues.live)||legacyStatus.equals(StaticValues.preview)||legacyStatus.equals(StaticValues.ondemand))){
                        UserAccountLiveDataDto userAccountLiveDataDto = new UserAccountLiveDataDto();
                        userAccountLiveDataDto.setFirstName(userInfoNode.path(StaticValues.firstName).asText());
                        userAccountLiveDataDto.setLastName(userInfoNode.path(StaticValues.lastName).asText());
                        String email = userInfoNode.path(StaticValues.email).asText();
                        userAccountLiveDataDto.setEmail(userInfoNode.path(StaticValues.email).asText());
                        String webcastId = webCastNode.path(StaticValues.webcastId).asText();
                        userAccountLiveDataDto.setWebcastTitle(webCastNode.path(StaticValues.title).asText());

                        switch (legacyStatus){
                            case "live":{
                                userAccountLiveDataDto.setLive("YES");
                                break;
                            }
                            case "preview":{
                                userAccountLiveDataDto.setPreview("YES");
                                break;
                            }
                            case "ondemand":{
                                userAccountLiveDataDto.setOnDemand("YES");
                                break;
                            }
                        }
                        userAccountLiveDataDtoMap.put(Pair.of(email, webcastId), userAccountLiveDataDto);
                        webcastIdList.add(Integer.valueOf(webcastId));
                    }
                    else if (userInfo != null && userAccountLiveDataDtoMap.containsKey(Pair.of(userInfoNode.path(StaticValues.email).asText(),webCastNode.path(StaticValues.webcastId).asText()))){
                        String email = userInfoNode.path(StaticValues.email).asText();
                        String webcastId =  webCastNode.path(StaticValues.webcastId).asText();
                        UserAccountLiveDataDto userAccountLiveDataDto = userAccountLiveDataDtoMap.get(Pair.of(email,webcastId));
                        switch (legacyStatus){
                            case "live":{
                                userAccountLiveDataDto.setLive("YES");
                                break;
                            }
                            case "preview":{
                                userAccountLiveDataDto.setPreview("YES");
                                break;
                            }
                            case "ondemand":{
                                userAccountLiveDataDto.setOnDemand("YES");
                                break;
                            }
                        }
                        userAccountLiveDataDtoMap.put(Pair.of(email, webcastId), userAccountLiveDataDto);
                        webcastIdList.add(Integer.valueOf(webcastId));
                    }
                }
                //For map viewerid with (email , webcastId) pair
                Map<Pair<String,String>,Integer> viewerIdMap = new HashMap<>();
                List<Viewer> viewerList = viewerRepository.findViewerByWebcastIdList(webcastIdList);
                if(viewerList != null){
                    for (Viewer viewer : viewerList){
                        if(viewer.getEmail()!=null && viewer.getStst_registered_webcastId()!=0){
                            Pair<String,String> pair = Pair.of(viewer.getEmail(),String.valueOf(viewer.getStst_registered_webcastId()));
                            if(!viewerIdMap.containsKey(pair)){
                                viewerIdMap.put(pair,viewer.getId());
                            }
                        }
                    }
                }
                //For calculating visit duration for  viewer id and storing viewer id and visit duration map
                List<Integer> viewerIdList = new ArrayList<>(viewerIdMap.values());
                List<Visit> visitList = null;
                if(dateRange != null){
                    Date startDate = new SimpleDateFormat(StaticValues.simpleDateFormat).parse(dateRange.get(0));
                    Date endDate = new SimpleDateFormat(StaticValues.simpleDateFormat).parse(dateRange.get(1));
                    visitList = visitRepository.findByViewerIdListDateRange(viewerIdList,startDate,endDate);
                }
                else {
                    visitList = visitRepository.findByViewerIdList(viewerIdList);
                }
                Map<Integer,Long> visitDurationMap = new HashMap<>();
                if(!visitList.isEmpty()){
                    for (Visit visit : visitList){
                        if(visitDurationMap.containsKey(visit.getViewerId())){
                            long oldDuration = visitDurationMap.get(visit.getViewerId());
                            long duration = Math.abs(visit.getStartTime().getTime()-visit.getEndTime().getTime());
                            visitDurationMap.replace(visit.getViewerId(),oldDuration+duration);
                        }
                        else {
                            long duration = Math.abs(visit.getStartTime().getTime()-visit.getEndTime().getTime());
                            visitDurationMap.put(visit.getViewerId(),duration);
                        }
                    }
                }
                for (Pair<String,String> key : userAccountLiveDataDtoMap.keySet()) {
                    UserAccountLiveDataDto userAccountLiveDataDto = userAccountLiveDataDtoMap.get(key);
                    if(viewerIdMap.containsKey(key)){
                        Integer viewerId = viewerIdMap.get(key);
                        if(visitDurationMap.containsKey(viewerId)){
                            long duration = visitDurationMap.get(viewerId);
                            userAccountLiveDataDto.setVisitDuration(duration/60000);
                        }
                    }
                    userAccountLiveDataDtoList.add(userAccountLiveDataDto);
                }
            }
        }catch (JsonProcessingException e){
            log.error(e.getMessage());
        }catch (ParseException e){
            log.error(e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return userAccountLiveDataDtoList;
    }


    public ResourceDownloadReportDto getUserInfoForResourceReport(List<String> organizationIdList, List<String> clientIdList, List<String> portalIdList, List<String> dateRange) {
        List<EventTypes> eventList = List.of(EventTypes.DOWNLOAD);
        List<UserInfoForResourceDownloadReportDto> userInfoForResourceDownloadReportDtoList = new ArrayList<>();
        List<ResourceDto> resourceDtoList = new ArrayList<>();
        ResourceDownloadReportDto resourceDownloadReportDto = new ResourceDownloadReportDto();
        try {
            List<Stat> downloadData = this.getStatDataAfterFilter(eventList,organizationIdList,clientIdList,portalIdList,dateRange);
            if(downloadData != null) {
                for (Stat stat : downloadData) {
                    UserInfoForResourceDownloadReportDto userInfoForResourceDownloadReportDto = new UserInfoForResourceDownloadReportDto();
                    Object userInfo = stat.getUserInfo();
                    String userInfoString = objectMapper.writeValueAsString(userInfo);
                    JsonNode userInfoNode = objectMapper.readTree(userInfoString);
                    userInfoForResourceDownloadReportDto.setFirstName(userInfoNode.path(StaticValues.firstName).asText());
                    userInfoForResourceDownloadReportDto.setLastName(userInfoNode.path(StaticValues.lastName).asText());
                    userInfoForResourceDownloadReportDto.setEmail(userInfoNode.path(StaticValues.email).asText());
                    userInfoForResourceDownloadReportDto.setTitle(stat.getResourceTitle());
                    userInfoForResourceDownloadReportDto.setResource(stat.getResourceName());
                    userInfoForResourceDownloadReportDto.setButtonEvent(stat.getButtonEvent());
                    userInfoForResourceDownloadReportDto.setResourceId(stat.getResourceID());

                    ResourceDto resourceDto = new ResourceDto();
                    if (userInfoForResourceDownloadReportDto.getTitle() != null) {
                        resourceDto.setResourceId(userInfoForResourceDownloadReportDto.getResourceId());
                        resourceDto.setTitle(userInfoForResourceDownloadReportDto.getTitle());
                    } else {
                        resourceDto.setResourceId(userInfoForResourceDownloadReportDto.getResourceId());
                        resourceDto.setTitle(userInfoForResourceDownloadReportDto.getResource());
                    }
                    if (!resourceDto.isAllFieldNull()) {
                        resourceDtoList.add(resourceDto);
                    }
                    userInfoForResourceDownloadReportDtoList.add(userInfoForResourceDownloadReportDto);
                }
            }

            Map<String,String> resourceMap = new HashMap<>();

            for (ResourceDto resourceDto : resourceDtoList) {
                String resourceId = resourceDto.getResourceId();
                resourceMap.put(resourceId, resourceDto.getTitle());
            }

            Integer count = resourceMap.size();

            DynamicColumnDto dynamicColumn = new DynamicColumnDto();
            dynamicColumn.setColumn(resourceMap.values());
            dynamicColumn.setColumnCount(count);


            resourceDownloadReportDto.setUserInfoForResourceDownloadReport(userInfoForResourceDownloadReportDtoList);
            resourceDownloadReportDto.setHeader(dynamicColumn);

        }catch (ParseException e){
            log.error(e.getMessage());
        }catch (JsonProcessingException e){
            log.error(e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return resourceDownloadReportDto;
    }

    public  List<UserInfoForDeviceInfoDto> getUserInfoForDeviceInfoReport(List<String> organizationIdList, List<String> clientIdList, List<String> portalIdList, List<String> dateRange) {

        List<EventTypes> eventList = List.of(EventTypes.DEVICE_INFO);
        List<UserInfoForDeviceInfoDto> userInfoForDeviceInfoDtoList = new ArrayList<>();
        try {
            List<Stat> statList = this.getStatDataAfterFilter(eventList,organizationIdList,clientIdList,portalIdList,dateRange);
            if(statList != null) {
                for (Stat stat : statList) {
                    Object userInfo = stat.getUserInfo();
                    Object deviceInfo = stat.getDeviceInfo();

                    String userInfoString = objectMapper.writeValueAsString(userInfo);
                    JsonNode userInfoNode = objectMapper.readTree(userInfoString);

                    String deviceInfoString = objectMapper.writeValueAsString(deviceInfo);
                    JsonNode deviceInfoNode = objectMapper.readTree(deviceInfoString);

                    UserInfoForDeviceInfoDto userInfoForDeviceInfoDto = new UserInfoForDeviceInfoDto();

                    userInfoForDeviceInfoDto.setFirstName(userInfoNode.path(StaticValues.firstName).asText());
                    userInfoForDeviceInfoDto.setLastName(userInfoNode.path(StaticValues.lastName).asText());
                    userInfoForDeviceInfoDto.setEmail(userInfoNode.path(StaticValues.email).asText());
                    userInfoForDeviceInfoDto.setDeviceType(deviceInfoNode.path("device").path("type").asText());
                    userInfoForDeviceInfoDto.setBrowser(deviceInfoNode.path("client").path("name").asText());
                    userInfoForDeviceInfoDto.setIp(stat.getIp());

                    userInfoForDeviceInfoDtoList.add(userInfoForDeviceInfoDto);
                }
            }
        }catch (ParseException e){
            log.error(e.getMessage());
        }catch (JsonProcessingException e){
            log.error(e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return userInfoForDeviceInfoDtoList;
    }


    //SERVICE FOR QNA
    public List<UserInfoQuestionReportDto> getUserInfoForQuestionReport(List<String> organizationList,List<String> clientList,List<String> portalList,List<String> dateRange) {

        List<EventTypes> eventList= List.of(EventTypes.WATCH_LIVE);
        List<UserInfoQuestionReportDto> userInfoQuestionReportList=new ArrayList<>();
        Map<Pair<String,String>,UserInfoQuestionReportDto> userInfoForQuestionReportDtoMap = new HashMap<>();

        try {
            List<Stat> watchData = this.getStatDataAfterFilter(eventList,organizationList,clientList,portalList,dateRange);
            if(watchData != null) {
                for (Stat stat : watchData) {
                    Object userInfo = stat.getUserInfo();
                    Object webCast = stat.getWebcast();

                    String userInfoString = objectMapper.writeValueAsString(userInfo);
                    String webCastString = objectMapper.writeValueAsString(webCast);

                    JsonNode userInfoNode = objectMapper.readTree(userInfoString);
                    JsonNode webCastNode = objectMapper.readTree(webCastString);

                    UserInfoQuestionReportDto userInfoQuestionReportDto = new UserInfoQuestionReportDto();
                    userInfoQuestionReportDto.setFirstName(userInfoNode.path(StaticValues.firstName).asText());
                    userInfoQuestionReportDto.setLastName(userInfoNode.path(StaticValues.lastName).asText());
                    String email = userInfoNode.path(StaticValues.email).asText();
                    userInfoQuestionReportDto.setEmail(userInfoNode.path(StaticValues.email).asText());
                    String webcastId = webCastNode.path(StaticValues.webcastId).asText();
                    userInfoQuestionReportDto.setWebcastName(webCastNode.path(StaticValues.title).asText());
                    userInfoQuestionReportDto.setQuestions(new ArrayList<>());
                    userInfoForQuestionReportDtoMap.put(Pair.of(email, webcastId), userInfoQuestionReportDto);

                }
                List<Integer> webcastIdList = new ArrayList<>();
                for(Pair<String,String> pair: userInfoForQuestionReportDtoMap.keySet()){
                    Integer webcastId = Integer.valueOf(pair.getSecond());
                    webcastIdList.add(webcastId);
                }
                List<Qanda> questionList = findQandaList(webcastIdList,dateRange);

                for(Qanda qanda : questionList){
                    String email = qanda.getEmail();
                    String webcastId = String.valueOf(qanda.getBC_ID());
                    Pair<String,String> pair = Pair.of(email,webcastId);

                    if(userInfoForQuestionReportDtoMap.containsKey(pair)){
                        UserInfoQuestionReportDto userInfoQuestionReportDto = userInfoForQuestionReportDtoMap.get(pair);
                        List<String> questions = userInfoQuestionReportDto.getQuestions();
                        String decodedQuestion = java.net.URLDecoder.decode(qanda.getQuestion(), StandardCharsets.UTF_8);
                        questions.add(decodedQuestion);
                        userInfoQuestionReportDto.setQuestions(questions);
                        userInfoForQuestionReportDtoMap.put(pair,userInfoQuestionReportDto);
                    }
                }
            }

            for(UserInfoQuestionReportDto userInfoQuestionReportDto : userInfoForQuestionReportDtoMap.values()){
                if(!userInfoQuestionReportDto.getQuestions().isEmpty()) {
                    userInfoQuestionReportList.add(userInfoQuestionReportDto);
                }

            }
        }catch (ParseException e){
            log.error(e.getMessage());
        }catch (JsonProcessingException e){
            log.error(e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return userInfoQuestionReportList;
    }

    public List<DemoGraphicsDto> getDemographicsChart(List<String> organizationIdList, List<String> clientIdList, List<String> portalIdList, List<String> dateRange) {

        List<EventTypes> eventList = List.of(EventTypes.LOGIN_SUCCESS);
        Map<Pair<String,String>,DemoGraphicsDto> demoGraphicsDtoMap = new HashMap<>();
        List<DemoGraphicsDto> demographicsChartDtoList = new ArrayList<>();
        try{
            List<Stat> statList = this.getStatDataAfterFilter(eventList,organizationIdList,clientIdList,portalIdList,dateRange);
            if(statList != null) {
                for (Stat stat : statList) {
                    DemoGraphicsDto demographicsChartDto = new DemoGraphicsDto();
                    Object userInfo = stat.getUserInfo();
                    Object attendeeLocationInfo = stat.getAttendees_location_info();

                    String userInfoString = objectMapper.writeValueAsString(userInfo);
                    JsonNode userInfoNode = objectMapper.readTree(userInfoString);

                    String attendeeLocationInfoString = objectMapper.writeValueAsString(attendeeLocationInfo);
                    JsonNode attendeeLocationInfoNode = objectMapper.readTree(attendeeLocationInfoString);

                    demographicsChartDto.setFirstName(userInfoNode.path(StaticValues.firstName).asText());
                    demographicsChartDto.setLastName(userInfoNode.path(StaticValues.lastName).asText());
                    demographicsChartDto.setEmail(userInfoNode.path(StaticValues.email).asText());
                    String email = demographicsChartDto.getEmail();
                    demographicsChartDto.setAs(attendeeLocationInfoNode.path("as").asText());
                    demographicsChartDto.setCity(attendeeLocationInfoNode.path("city").asText());
                    demographicsChartDto.setCountry(attendeeLocationInfoNode.path("country").asText());
                    String country = demographicsChartDto.getCountry();
                    demographicsChartDto.setCountryCode(attendeeLocationInfoNode.path("countryCode").asText());
                    demographicsChartDto.setIsp(attendeeLocationInfoNode.path("isp").asText());
                    demographicsChartDto.setLat(attendeeLocationInfoNode.path("lat").asText());
                    demographicsChartDto.setLon(attendeeLocationInfoNode.path("lon").asText());
                    demographicsChartDto.setOrg(attendeeLocationInfoNode.path("org").asText());
                    demographicsChartDto.setQuery(attendeeLocationInfoNode.path("query").asText());
                    demographicsChartDto.setRegion(attendeeLocationInfoNode.path("region").asText());
                    demographicsChartDto.setRegionName(attendeeLocationInfoNode.path("regionName").asText());
                    demographicsChartDto.setStatus(attendeeLocationInfoNode.path("status").asText());
                    demographicsChartDto.setTimezone(attendeeLocationInfoNode.path("timezone").asText());
                    demographicsChartDto.setZip(attendeeLocationInfoNode.path("zip").asText());

                    demoGraphicsDtoMap.put(Pair.of(email, country), demographicsChartDto);
                }

            }
            for(DemoGraphicsDto demographicsChartDto:demoGraphicsDtoMap.values()){
                demographicsChartDtoList.add(demographicsChartDto);
            }
        }catch (ParseException e){
            log.error(e.getMessage());
        }catch (JsonProcessingException e){
            log.error(e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return demographicsChartDtoList;
    }


    //ENDPOINT SERVICE FOR GRAPH STATISTICS DATA
    public GraphBarChartDto graphBarChart(List<String> organizationList, List<String> clientList, List<String> portalList, List<String> dateRange) {
        List<String> selectedPortalId = new ArrayList<>();
        selectedPortalId = this.filterPortalId(organizationList,clientList,portalList);
        List<EventTypes> eventList = List.of(EventTypes.WATCH_LIVE);
        GraphBarChartDto graphDto = new GraphBarChartDto();

        try{
            List<Stat> statList = this.getStatDataAfterFilter(eventList,organizationList,clientList,portalList,dateRange);
            String queryDownloadCount = "{aggregate: 'stat', pipeline:[{\n" +
                    "    $match: {\n" +
                    "        buttonEvent: 'DOWNLOAD',\n" +
                    "        portalId: {\n" +
                    "            $in: ['" + String.join("','", selectedPortalId) + "'] \n" +
                    "        }\n" +this.dateRangePipeline(dateRange)+
                    "    }\n" +
                    "}, {\n" +
                    "    $group: {\n" +
                    "        _id: null,\n" +
                    "        total: {\n" +
                    "            $sum: '$count'\n" +
                    "        }\n" +
                    "    }\n" +
                    "}], cursor: {}}";
            String queryWatchLiveCount = "{aggregate: 'stat', pipeline:[{$match: {\n" +
                    " buttonEvent: 'WATCH_LIVE',\n" +
                    " portalId: {\n" +
                    " $in: ['" + String.join("','", selectedPortalId) + "'] \n" +
                    " },\n" +
                    " 'webcast.legacyStatus': 'live'\n" + this.dateRangePipeline(dateRange)+
                    "}}, {$group: {\n" +
                    " _id: {\n" +
                    "  email: '$userInfo.email',\n" +
                    "  webcast: '$webcast.webcastId'\n" +
                    " }\n" +
                    "}}, {$group: {\n" +
                    " _id: null,\n" +
                    " total: {\n" +
                    "  $sum: 1\n" +
                    " }\n" +
                    "}}], cursor: {}}";
            String queryVideoWatchedCount = "{aggregate: 'stat', pipeline:[{\n" +
                    "    $match: {\n" +
                    "        buttonEvent: 'VIDEO_WATCH_DURATION',\n" +
                    "        portalId: {\n" +
                    "            $in: ['" + String.join("','", selectedPortalId) + "'] \n" +
                    "        }\n" +this.dateRangePipeline(dateRange)+
                    "    }\n" +
                    "}, {\n" +
                    "    $group: {\n" +
                    "        _id: null,\n" +
                    "        total: {\n" +
                    "            $sum: 1 \n" +
                    "        }\n" +
                    "    }\n" +
                    "}], cursor: {}}";
            Set<String> webcastList = new HashSet<>();
            Set<String> emailList = new HashSet<>();
            for (Stat stat : statList) {
                String webCastString = objectMapper.writeValueAsString(stat.getWebcast());
                String userInfoString = objectMapper.writeValueAsString(stat.getUserInfo());
                JsonNode webCastNode = objectMapper.readTree(webCastString);
                JsonNode userInfoNode = objectMapper.readTree(userInfoString);
                String webcastId = webCastNode.path(StaticValues.webcastId).asText();
                String email = userInfoNode.path(StaticValues.email).asText();
                webcastList.add(Integer.valueOf(webcastId).toString());
                if(email.contains("'"))
                    email = email.replace("'","\\'");
                emailList.add(email);
            }
            String queryQuestionCount = "{aggregate: 'qanda', pipeline:[{\n" +
                    "    $match: {\n" +
                    "        BC_ID: {\n" +
                    "            $in:[" + String.join(",", webcastList) + "] \n" +
                    "        },\n" +
                    "        email: {\n" +
                    "            $in:['" + String.join("','", emailList) + "'] \n" +
                    "        }\n" + this.dateRangePipelineForQuestion(dateRange) +
                    "    }\n" +
                    "}, {\n" +
                    "    $group: {\n" +
                    "        _id: null,\n" +
                    "        total: {\n" +
                    "            $sum: 1 \n" +
                    "        }\n" +
                    "    }\n" +
                    "}], cursor: {}}";
            graphDto.setTotalDownloads(this.grapBarChartExecuteCommand(queryDownloadCount));
            graphDto.setTotalLiveViews(this.grapBarChartExecuteCommand(queryWatchLiveCount));
            graphDto.setTotalVideoWatched(this.grapBarChartExecuteCommand(queryVideoWatchedCount));
            graphDto.setTotalQuestionsAsked(this.grapBarChartExecuteCommand(queryQuestionCount));
        }catch (ParseException e){
            log.error(e.getMessage());
        }catch (JsonProcessingException e){
            log.error(e.getMessage());
        }catch (Exception e){
            e.getMessage();
        }

        return graphDto;
    }

    public List<UserInfoForWatchedVideoReportDto> getUserInfoForWatchedVideoReport(List<String> organizationIdList, List<String> clientIdList, List<String> portalIdList, List<String> dateRange) {

        List<EventTypes> eventList = List.of(EventTypes.VIDEO_WATCH_DURATION);
        List<Stat> statList = null;
        List<UserInfoForWatchedVideoReportDto> userInfoForWatchedVideoReportDtoList = new ArrayList<>();
        Map<Pair<String,String>,UserInfoForWatchedVideoReportDto> userInfoForWatchedVideoReportDtoMap = new HashMap<>();
        try{
            statList = this.getStatDataAfterFilter(eventList,organizationIdList,clientIdList,portalIdList,dateRange);
            if(statList != null) {
                for (Stat stat : statList) {
                    Object userInfo = stat.getUserInfo();
                    String userInfoString = objectMapper.writeValueAsString(userInfo);
                    JsonNode userInfoNode = objectMapper.readTree(userInfoString);
                    String email = userInfoNode.path(StaticValues.email).asText();
                    String videoId = stat.getVideoID();
                    if(email != null && videoId != null){
                        if(userInfoForWatchedVideoReportDtoMap.containsKey(Pair.of(videoId, email))){
                            double watchDuration = 0.0000000;
                            UserInfoForWatchedVideoReportDto userInfoForWatchedVideoReport = userInfoForWatchedVideoReportDtoMap.get(Pair.of(videoId, email));
                            watchDuration = Double.parseDouble(stat.getDuration()) + userInfoForWatchedVideoReport.getDuration();
                            userInfoForWatchedVideoReport.setDuration(watchDuration);
                            userInfoForWatchedVideoReportDtoMap.put(Pair.of(videoId, email),userInfoForWatchedVideoReport);
                        }else{
                            UserInfoForWatchedVideoReportDto userInfoForWatchedVideoReportDto = new UserInfoForWatchedVideoReportDto();
                            userInfoForWatchedVideoReportDto.setFirstName(userInfoNode.path(StaticValues.firstName).asText());
                            userInfoForWatchedVideoReportDto.setLastName(userInfoNode.path(StaticValues.lastName).asText());
                            userInfoForWatchedVideoReportDto.setEmail(userInfoNode.path(StaticValues.email).asText());

                            userInfoForWatchedVideoReportDto.setVideoName(stat.getVideoName());
                            userInfoForWatchedVideoReportDto.setDuration(Double.parseDouble(stat.getDuration()));
                            userInfoForWatchedVideoReportDtoMap.put(Pair.of(videoId, email), userInfoForWatchedVideoReportDto);
                        }
                    }

                }
            }
            for (UserInfoForWatchedVideoReportDto userInfoForWatchedVideoReportDto : userInfoForWatchedVideoReportDtoMap.values()) {
                if(userInfoForWatchedVideoReportDto.getDuration()!=null){
                    double durationInSec = userInfoForWatchedVideoReportDto.getDuration();
                    durationInSec = (durationInSec / 60.00);
                    Double durationInMinute = Double.parseDouble(new DecimalFormat("##.##").format(durationInSec));
                    userInfoForWatchedVideoReportDto.setDuration(durationInMinute);
                }
                userInfoForWatchedVideoReportDtoList.add(userInfoForWatchedVideoReportDto);
            }
        }catch (ParseException e){
            log.error(e.getMessage());
        }
        catch (JsonProcessingException e){
            log.error(e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return userInfoForWatchedVideoReportDtoList;
    }

    //OVERVIEW REPORT TILES
    public List<UserInfoOverviewReportDto> overviewReports(List<String> organizationIdList, List<String> clientIdList, List<String> portalIdList, List<String> dateRange) {

        List<EventTypes> eventList = List.of(EventTypes.WATCH_LIVE,EventTypes.LOGIN_SUCCESS,
                EventTypes.REGISTRATION_SUCCESS,EventTypes.DOWNLOAD,EventTypes.HOME,
                EventTypes.SESSIONS,EventTypes.RESOURCES,EventTypes.VIDEO_WATCH_DURATION,
                EventTypes.ONDEMAND,EventTypes.VISIT_DURATION,EventTypes.RE_PLAYING,EventTypes.AGENDA_SPEAKERS,EventTypes.VIDEO_LIBRARY);
        List<Stat> statList = null;
        List<Integer> webcastIdList = new ArrayList<>();
        Map<String,UserInfoOverviewReportDto> userInfoOverviewReportDtoMap = new HashMap<>();
        List<UserInfoOverviewReportDto> userInfoOverviewReportDtoList = new ArrayList<>();
        try {
            statList = this.getStatDataAfterFilter(eventList,organizationIdList,clientIdList,portalIdList,dateRange);
        }catch (ParseException e){
            log.error(e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage());
        }
        if(statList!=null){
            for(Stat stat : statList){
                try {
                    Object userInfo = stat.getUserInfo();
                    Object webCast = stat.getWebcast();
                    String userInfoString = objectMapper.writeValueAsString(userInfo);
                    String webCastString = objectMapper.writeValueAsString(webCast);

                    JsonNode userInfoNode = objectMapper.readTree(userInfoString);
                    JsonNode webCastNode = objectMapper.readTree(webCastString);
                    String email = userInfoNode.path(StaticValues.email).asText();

                    if(!userInfoOverviewReportDtoMap.containsKey(email) && userInfo != null){
                        UserInfoOverviewReportDto userInfoOverviewReportDto = new UserInfoOverviewReportDto();
                        userInfoOverviewReportDto.setFirstname(userInfoNode.path(StaticValues.firstName).asText());
                        userInfoOverviewReportDto.setLastName(userInfoNode.path(StaticValues.lastName).asText());
                        userInfoOverviewReportDto.setEmail(userInfoNode.path(StaticValues.email).asText());
                        userInfoOverviewReportDto.setQuestions(new ArrayList<>());
                        if(stat.getButtonEvent().equals(EventTypes.REGISTRATION_SUCCESS)){
                            userInfoOverviewReportDto.setDateAndTimeRegistraton(userInfoNode.path(StaticValues.created).asText());
                        }
                        if(stat.getButtonEvent().equals(EventTypes.DOWNLOAD)){
                            userInfoOverviewReportDto.setDownloads(String.valueOf(stat.getCount()));
                        }
                        if(stat.getButtonEvent().equals(EventTypes.HOME)){
                            userInfoOverviewReportDto.setHomeVisited(String.valueOf(stat.getCount()));
                        }
                        if(stat.getButtonEvent().equals(EventTypes.WATCH_LIVE)){
                            webcastIdList.add(Integer.valueOf(webCastNode.path(StaticValues.webcastId).asText()));
                        }
                        if(stat.getButtonEvent().equals(EventTypes.RE_PLAYING)){
                            userInfoOverviewReportDto.setReplayWebcastWatched(String.valueOf(stat.getCount()));
                        }
                        if(stat.getButtonEvent().equals(EventTypes.VIDEO_LIBRARY)){
                            userInfoOverviewReportDto.setVideoLibraryPageVisited(String.valueOf(stat.getCount()));
                        }
                        if(stat.getButtonEvent().equals(EventTypes.AGENDA_SPEAKERS)){
                            userInfoOverviewReportDto.setAgendaSpeaker(String.valueOf(stat.getCount()));
                        }
                        if(stat.getButtonEvent().equals(EventTypes.SESSIONS)){
                            userInfoOverviewReportDto.setSessionsVisited(String.valueOf(stat.getCount()));
                        }
                        if(stat.getButtonEvent().equals(EventTypes.RESOURCES)){
                            userInfoOverviewReportDto.setResourceVisited(String.valueOf(stat.getCount()));
                        }
                        if(stat.getButtonEvent().equals(EventTypes.LOGIN_SUCCESS)){
                            userInfoOverviewReportDto.setPortalVisits(String.valueOf(stat.getCount()));
                        }
                        if(stat.getButtonEvent().equals(EventTypes.VIDEO_WATCH_DURATION)){
                            userInfoOverviewReportDto.setVideosWatched(String.valueOf("1"));
                        }
                        if(stat.getButtonEvent().equals(EventTypes.VISIT_DURATION) && (stat.getLoginTime() != null && stat.getLogoutTime() != null)){
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            Date dateLogin = format.parse(stat.getLoginTime());
                            Date dateLogout = format.parse(stat.getLogoutTime());
                            Long duration=Math.abs(dateLogin.getTime() - dateLogout.getTime());
                            Double totalInMin = (duration/60000.00);
                            userInfoOverviewReportDto.setCumulativeAttendanceDuration(String.valueOf(totalInMin));
                        }
                        userInfoOverviewReportDtoMap.put(email,userInfoOverviewReportDto);
                    }else if(userInfoOverviewReportDtoMap.containsKey(email)){
                        if(stat.getButtonEvent().equals(EventTypes.REGISTRATION_SUCCESS)){
                            UserInfoOverviewReportDto userInfoOverviewReportDto = userInfoOverviewReportDtoMap.get(email);
                            userInfoOverviewReportDto.setDateAndTimeRegistraton(userInfoNode.path(StaticValues.created).asText());
                            userInfoOverviewReportDtoMap.put(email,userInfoOverviewReportDto);
                        }
                        if(stat.getButtonEvent().equals(EventTypes.DOWNLOAD)){
                            UserInfoOverviewReportDto userInfoOverviewReportDto = userInfoOverviewReportDtoMap.get(email);
                            int total=0;
                            total=Integer.valueOf(userInfoOverviewReportDto.getDownloads())+stat.getCount();
                            userInfoOverviewReportDto.setDownloads(String.valueOf(total));
                            userInfoOverviewReportDtoMap.put(email,userInfoOverviewReportDto);
                        }
                        if(stat.getButtonEvent().equals(EventTypes.HOME)){
                            UserInfoOverviewReportDto userInfoOverviewReportDto = userInfoOverviewReportDtoMap.get(email);
                            int total=0;
                            total=Integer.valueOf(userInfoOverviewReportDto.getHomeVisited())+stat.getCount();
                            userInfoOverviewReportDto.setHomeVisited(String.valueOf(total));
                            userInfoOverviewReportDtoMap.put(email,userInfoOverviewReportDto);
                        }
                        if(stat.getButtonEvent().equals(EventTypes.WATCH_LIVE)){
                            webcastIdList.add(Integer.valueOf(webCastNode.path(StaticValues.webcastId).asText()));
                        }
                        if(stat.getButtonEvent().equals(EventTypes.RE_PLAYING)){
                            UserInfoOverviewReportDto userInfoOverviewReportDto = userInfoOverviewReportDtoMap.get(email);
                            int total=0;
                            total=Integer.valueOf(userInfoOverviewReportDto.getReplayWebcastWatched())+stat.getCount();
                            userInfoOverviewReportDto.setReplayWebcastWatched(String.valueOf(total));
                            userInfoOverviewReportDtoMap.put(email,userInfoOverviewReportDto);
                        }
                        if(stat.getButtonEvent().equals(EventTypes.AGENDA_SPEAKERS)){
                            UserInfoOverviewReportDto userInfoOverviewReportDto = userInfoOverviewReportDtoMap.get(email);
                            int total=0;
                            total=Integer.valueOf(userInfoOverviewReportDto.getAgendaSpeaker())+stat.getCount();
                            userInfoOverviewReportDto.setAgendaSpeaker(String.valueOf(total));
                            userInfoOverviewReportDtoMap.put(email,userInfoOverviewReportDto);
                        }
                        if(stat.getButtonEvent().equals(EventTypes.VIDEO_LIBRARY)){
                            UserInfoOverviewReportDto userInfoOverviewReportDto = userInfoOverviewReportDtoMap.get(email);
                            int total=0;
                            total=Integer.valueOf(userInfoOverviewReportDto.getVideoLibraryPageVisited())+stat.getCount();
                            userInfoOverviewReportDto.setVideoLibraryPageVisited(String.valueOf(total));
                            userInfoOverviewReportDtoMap.put(email,userInfoOverviewReportDto);
                        }
                        if(stat.getButtonEvent().equals(EventTypes.SESSIONS)){
                            UserInfoOverviewReportDto userInfoOverviewReportDto = userInfoOverviewReportDtoMap.get(email);
                            int total=0;
                            total=Integer.valueOf(userInfoOverviewReportDto.getSessionsVisited())+stat.getCount();
                            userInfoOverviewReportDto.setSessionsVisited(String.valueOf(total));
                            userInfoOverviewReportDtoMap.put(email,userInfoOverviewReportDto);
                        }
                        if(stat.getButtonEvent().equals(EventTypes.RESOURCES)){
                            UserInfoOverviewReportDto userInfoOverviewReportDto = userInfoOverviewReportDtoMap.get(email);
                            int total=0;
                            total=Integer.valueOf(userInfoOverviewReportDto.getResourceVisited())+stat.getCount();
                            userInfoOverviewReportDto.setResourceVisited(String.valueOf(total));
                            userInfoOverviewReportDtoMap.put(email,userInfoOverviewReportDto);
                        }
                        if(stat.getButtonEvent().equals(EventTypes.LOGIN_SUCCESS)){
                            UserInfoOverviewReportDto userInfoOverviewReportDto = userInfoOverviewReportDtoMap.get(email);
                            int total=0;
                            total=Integer.valueOf(userInfoOverviewReportDto.getPortalVisits())+stat.getCount();
                            userInfoOverviewReportDto.setPortalVisits(String.valueOf(total));
                            userInfoOverviewReportDtoMap.put(email,userInfoOverviewReportDto);
                        }
                        if(stat.getButtonEvent().equals(EventTypes.VIDEO_WATCH_DURATION)){
                            UserInfoOverviewReportDto userInfoOverviewReportDto = userInfoOverviewReportDtoMap.get(email);
                            int total=0;
                            total=Integer.valueOf(userInfoOverviewReportDto.getVideosWatched())+1;
                            userInfoOverviewReportDto.setVideosWatched(String.valueOf(total));
                            userInfoOverviewReportDtoMap.put(email,userInfoOverviewReportDto);
                        }
                        if(stat.getButtonEvent().equals(EventTypes.VISIT_DURATION) && (stat.getLoginTime() != null && stat.getLogoutTime() != null)){
                            UserInfoOverviewReportDto userInfoOverviewReportDto = userInfoOverviewReportDtoMap.get(email);
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            Date dateLogin = format.parse(stat.getLoginTime());
                            Date dateLogout = format.parse(stat.getLogoutTime());
                            Long duration=Math.abs(dateLogin.getTime() - dateLogout.getTime());
                            Double totalInMin = (duration/60000.00);
                            Double total = Double.valueOf(userInfoOverviewReportDto.getCumulativeAttendanceDuration()) + totalInMin;
                            userInfoOverviewReportDto.setCumulativeAttendanceDuration(String.valueOf(total));
                            userInfoOverviewReportDtoMap.put(email,userInfoOverviewReportDto);
                        }
                    }
                }catch (ParseException e){
                    log.error(e.getMessage());
                }catch (JsonProcessingException e){
                    log.error(e.getMessage());
                }catch (Exception e){
                    log.error(e.getMessage());
                }
            }
            List<Qanda> qandaList = findQandaList(webcastIdList,dateRange);
            for (Qanda qanda : qandaList){
                if(userInfoOverviewReportDtoMap.containsKey(qanda.getEmail())){
                    UserInfoOverviewReportDto userInfoOverviewReportDto = userInfoOverviewReportDtoMap.get(qanda.getEmail());
                    List<String> questions = userInfoOverviewReportDto.getQuestions();
                    String decodedQuestion = java.net.URLDecoder.decode(qanda.getQuestion(), StandardCharsets.UTF_8);
                    questions.add(decodedQuestion);
                    userInfoOverviewReportDtoMap.put(userInfoOverviewReportDto.getEmail(),userInfoOverviewReportDto);
                }
            }
            List<String> selectedPortalId = this.filterPortalId(organizationIdList,clientIdList,portalIdList);
            for (UserInfoOverviewReportDto userInfoOverviewReportDto : userInfoOverviewReportDtoMap.values()){
                try {
                    String email = userInfoOverviewReportDto.getEmail();
                    email = email.replace("'","\\'");
                    String queryWatchLiveCount = "{aggregate: 'stat', pipeline:[{\n" +
                            "    $match: {\n" +
                            "        buttonEvent: 'WATCH_LIVE',\n" +
                            "        portalId: {\n" +
                            "            $in: ['" + String.join("','", selectedPortalId) + "'] \n" +
                            "        },\n" +
                            "        'userInfo.email': '"+email+"'\n" +
                            "        ,'webcast.legacyStatus': 'live'\n" +this.dateRangePipeline(dateRange)+
                            "    }\n" +
                            "}, {\n" +
                            "    $group: {\n" +
                            "        _id: '$webcast.id'\n" +
                            "    }\n" +
                            "}, {\n" +
                            "    $group: {\n" +
                            "        _id: null,\n" +
                            "        total: {\n" +
                            "            $sum: 1\n" +
                            "        }\n" +
                            "    }\n" +
                            "}], cursor: {}}";
                    userInfoOverviewReportDto.setLiveWebcastAttended(this.grapBarChartExecuteCommand(queryWatchLiveCount).toString());
                    double doubleDurationFull = Double.valueOf(userInfoOverviewReportDto.getCumulativeAttendanceDuration());
                    double durationCut = Double.parseDouble(new DecimalFormat("##.##").format(doubleDurationFull));
                    userInfoOverviewReportDto.setCumulativeAttendanceDuration(String.valueOf(durationCut));

                    userInfoOverviewReportDtoList.add(userInfoOverviewReportDto);

                }catch (JsonProcessingException e){
                    log.error(e.getMessage());
                }catch (ParseException e){
                    log.error(e.getMessage());
                }catch (Exception e){
                    log.error(e.getMessage());
                }
            }
        }

        return userInfoOverviewReportDtoList;
    }

    private List<Stat> getStatDataAfterFilter(List<EventTypes> eventList ,List<String> organizationIdList,
                                      List<String> clientIdList, List<String> portalIdList, List<String> dateRange ) throws ParseException {
        List<Homepage> homepageList = null;

        if(organizationIdList !=null ){
            homepageList = homepageRepository.findByOrganizationAndClientAndPortal(organizationIdList, clientIdList, portalIdList);
        }else {
            homepageList = homepageRepository.findByClientAndPortal(clientIdList, portalIdList);
        }
        List<String> selectedPortalId = new ArrayList<>();
        if(!homepageList.isEmpty()){
            homepageList.forEach(x->selectedPortalId.add(x.getPortalId()));
        }else {
            log.error("The Portal Id is not associate with client Id and organization Id");
        }
        List<Stat> statList = null;
        if(dateRange !=null ){
            String startDate = new SimpleDateFormat(StaticValues.simpleDateFormat).format(new SimpleDateFormat(StaticValues.simpleDateFormat).parse(dateRange.get(0)));
            String endDate = new SimpleDateFormat(StaticValues.simpleDateFormat).format(new SimpleDateFormat(StaticValues.simpleDateFormat).parse(dateRange.get(1)));
            statList = statRepository.findByButtonEventListAndPortalIdListAndDateRange(eventList,selectedPortalId,startDate,endDate);
        }
        else {
            statList = statRepository.findByButtonEventListAndPortalIdList(eventList,selectedPortalId);
        }
        return statList;
    }

    private List<String> filterPortalId(List<String> organizationIdList,
                               List<String> clientIdList, List<String> portalIdList){
       List<Homepage> homepageList = null;
       if (organizationIdList != null) {
           homepageList = homepageRepository.findByOrganizationAndClientAndPortal(organizationIdList, clientIdList, portalIdList);
       } else {
           homepageList = homepageRepository.findByClientAndPortal(clientIdList, portalIdList);
       }
       List<String> selectedPortalId = new ArrayList<>();
       if(!homepageList.isEmpty()){
           homepageList.forEach(x->selectedPortalId.add(x.getPortalId()));
       }else {
           log.error("The Portal Id is not associate with client Id and organization Id");
       }
        return selectedPortalId;
   }

    private Integer grapBarChartExecuteCommand(String queryParticipants) throws JsonProcessingException {

        Document participantDoc = mongoTemplate.executeCommand(queryParticipants);
        String participantJson = participantDoc.toJson();
        JsonNode participantNode = objectMapper.readTree(participantJson);
        JsonNode participantCount = participantNode.path("cursor").path("firstBatch").get(0);

        return participantCount != null ? Integer.valueOf(participantCount.path("total").asText()):0;
    }

    private String    dateRangePipeline (List<String> dateRange) throws ParseException {
        String startDate = null;
        String endDate = null;
        if(dateRange != null){
            startDate = new SimpleDateFormat(StaticValues.simpleDateFormat).format(new SimpleDateFormat(StaticValues.simpleDateFormat).parse(dateRange.get(0)));
            endDate = new SimpleDateFormat(StaticValues.simpleDateFormat).format(new SimpleDateFormat(StaticValues.simpleDateFormat).parse(dateRange.get(1)));
        }
        String rangeString =  dateRange!=null?
                "        ,created: {\n"+
                        "            $gt: '"+startDate+"', \n" +
                        "             $lt: '"+endDate+"' \n" +
                        "        } \n"
                :"" ;
        return rangeString;
    }


    private String dateRangePipelineForQuestion (List<String> dateRange) throws ParseException {
        long startTimeUnix = 0;
        long endTimeUnix = 0;
        if(dateRange != null){
            Date dateFormat = new SimpleDateFormat(StaticValues.simpleDateFormat).parse(dateRange.get(0));
            startTimeUnix = dateFormat.getTime();
            dateFormat = new SimpleDateFormat(StaticValues.simpleDateFormat).parse(dateRange.get(1));
            endTimeUnix = dateFormat.getTime();
        }
        String rangeString =  dateRange!=null?
                "        ,QA_date: {\n"+
                        "            $gt: "+startTimeUnix+", \n" +
                        "             $lt: "+endTimeUnix+" \n" +
                        "        } \n"
                :"" ;
        return rangeString;
    }

    private List<Qanda> findQandaList(List<Integer> webcastIdList , List<String> dateRange){
        long startTimeUnix;
        long endTimeUnix;
        if(dateRange != null){
            try{
                Date dateFormat = new SimpleDateFormat(StaticValues.simpleDateFormat).parse(dateRange.get(0));
                startTimeUnix = dateFormat.getTime();
                dateFormat = new SimpleDateFormat(StaticValues.simpleDateFormat).parse(dateRange.get(1));
                endTimeUnix = dateFormat.getTime();
                return qandaRepository.findQandaByWebcastIdListDateRange(webcastIdList,startTimeUnix,endTimeUnix);
            }catch (Exception e){
                log.error("Fail to get Q&A result using dateRange with ->"+e.getMessage());
            }
        }
        return qandaRepository.findQandaByWebcastIdList(webcastIdList);
    }
}
