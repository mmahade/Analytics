package com.sharestudio.analytics.repository;

import com.sharestudio.analytics.entity.Qanda;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class QandaRepositoryTest {
	
	@Autowired
	private QandaRepository qandaRepository;

	@Test
	void findQandaByWebcastIdAndEmail() {
		List<Qanda> testQandaList=qandaRepository.findQandaByWebcastIdAndEmail(5943,"ignacio.cerezo@jpmorgan.com");
	    assertEquals(testQandaList.get(0).getBC_ID(),5943);
	    assertEquals(testQandaList.get(0).getEmail(),"ignacio.cerezo@jpmorgan.com");
		
	}

}
