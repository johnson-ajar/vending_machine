package com.machine.vending.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.machine.vending.app.config.VendingMachineEntityModule;
import com.machine.vending.model.common.CoinType;
import com.machine.vending.model.entity.CoinRegistryEntity;
import com.machine.vending.model.entity.VendingMachineEntity;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
		VendingApplication.class
},webEnvironment = WebEnvironment.DEFINED_PORT)
@TestPropertySource("classpath:config/application.yml")
@ActiveProfiles({"test"})
public class VendingApplicationTest {
	@Value("${local.server.port}")
	private int port;
	
	private HttpEntity<String> headerEntity;
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@BeforeEach
	public void setUp() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headerEntity = new HttpEntity<String>(headers);
	}
	
	@Test
	public void testAllMachines() throws Exception {
		String url = "/vending/machine/all";	
		ResponseEntity<VendingMachineEntity[]> r = testRestTemplate.exchange(url, HttpMethod.GET, headerEntity, VendingMachineEntity[].class);
		VendingMachineEntity[] entities = r.getBody();
		assertTrue(r.getStatusCode().value() == HttpStatus.OK.value());
		assertEquals(entities.length,2);
		assertEquals(entities[0].getName(), "machine2");
		assertEquals(entities[1].getName(), "machine1");
		assertEquals(entities[0].getBank().getMachineRegistry().getAmount(), 100.0);
		assertEquals(entities[0].getBank().getUserRegistry().getAmount(), 0.0);
	}
	
	@Test
	public void testGetMachine() {
		String url = "/vending/machine/machine1";
		ResponseEntity<VendingMachineEntity> r = testRestTemplate.exchange(url, HttpMethod.GET, headerEntity, VendingMachineEntity.class);
		VendingMachineEntity entity = r.getBody();
		assertTrue(r.getStatusCode().value() == HttpStatus.OK.value());
		assertEquals(entity.getName(), "machine1");
		
		url = "/vending/machine/machine11";
		r = testRestTemplate.exchange(url, HttpMethod.GET, headerEntity, VendingMachineEntity.class);
		assertTrue(r.getStatusCode().value() == HttpStatus.NOT_FOUND.value());
		assertNull(r.getBody());
		
		url = "/vending/machine/";
		r = testRestTemplate.exchange(url, HttpMethod.GET, headerEntity, VendingMachineEntity.class);
		assertTrue(r.getStatusCode().value() == HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void testGetMachineRegistry() {
		String url = "/vending/machine/machine1/coins";
		ResponseEntity<CoinRegistryEntity> r = testRestTemplate.exchange(url, HttpMethod.GET, headerEntity, CoinRegistryEntity.class);
		CoinRegistryEntity registry = r.getBody();
		assertEquals(registry.getAmount(), 100.0);
		
		url = "/vending/machine/machine/coins";
		r = testRestTemplate.exchange(url, HttpMethod.GET, headerEntity, CoinRegistryEntity.class);
		assertTrue(r.getStatusCode().value() == HttpStatus.NOT_FOUND.value());
		assertNull(r.getBody());
	}
	
	@Test
	public void testGetUserRegistry() {
		String url = "/vending/machine/machine1/user/coins";
		ResponseEntity<CoinRegistryEntity> r = testRestTemplate.exchange(url, HttpMethod.GET, headerEntity, CoinRegistryEntity.class);
		CoinRegistryEntity registry = r.getBody();
		assertEquals(registry.getAmount(), 0.0);
		
		url = "/vending/machine/machine/user/coins";
		r = testRestTemplate.exchange(url, HttpMethod.GET, headerEntity, CoinRegistryEntity.class);
		assertTrue(r.getStatusCode().value() == HttpStatus.NOT_FOUND.value());
		assertNull(r.getBody());
		
	}
	
	@Disabled
	public void testUpdateRegistry() {
		String url = "/vending/machine/machine1/registry?type=machine";
		CoinRegistryEntity registry = new CoinRegistryEntity();
		Map<CoinType, Integer> coins = new HashMap<CoinType, Integer>();
		coins.put(CoinType.ONE_POUND, 10);
		registry.setCoinRegistry(coins);
		testRestTemplate.put(url, registry);
		url = "/vending/machine/machine1/coins";
		ResponseEntity r = testRestTemplate.exchange(url, HttpMethod.GET, headerEntity, CoinRegistryEntity.class);
		
	}
	
	/*
	 * The custom json serializer and deserializer needs extensive testing, will be 
	 * handled later.
	 */
	private MappingJackson2HttpMessageConverter  getConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		VendingMachineEntityModule machineModule = new VendingMachineEntityModule();
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(machineModule);
		converter.setObjectMapper(mapper);
		return converter;
	}
	
}
