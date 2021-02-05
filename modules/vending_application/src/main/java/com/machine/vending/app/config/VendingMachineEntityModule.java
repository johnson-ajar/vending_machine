package com.machine.vending.app.config;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.machine.vending.model.CoinTypeFactory;
import com.machine.vending.model.api.CoinRegistry;
import com.machine.vending.model.common.CoinGroup;
import com.machine.vending.model.entity.CoinRegistryEntity;
import com.machine.vending.model.entity.VendingBankEntity;
import com.machine.vending.model.entity.VendingMachineEntity;


public class VendingMachineEntityModule extends SimpleModule{
	
	private static final long serialVersionUID = 1L;
	
	public VendingMachineEntityModule() {
		this.addDeserializer(VendingMachineEntity.class, new VendingMachineEntityDeserializer());
		this.addSerializer(VendingMachineEntity.class, new VendingMachineEntitySerializer());
		this.addDeserializer(VendingBankEntity.class, new VendingBankEntityDeserializer());
	}

}

final class CoinRegistryEntityDeserializer extends JsonDeserializer<CoinRegistryEntity> {

	@Override
	public CoinRegistryEntity deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		CoinRegistry registry = CoinTypeFactory.createRegistry(CoinGroup.UK, true);
		
		return null;
	}
	
}
final class VendingBankEntityDeserializer extends JsonDeserializer<VendingBankEntity> {

	@Override
	public VendingBankEntity deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		System.out.println("Deserializing Vending bank object");
		ObjectCodec objCodec = p.getCodec();
		JsonNode jNode = objCodec.readTree(p);
		VendingBankEntity entity = new VendingBankEntity();
		return entity;
	}
	
}

final class VendingMachineEntityDeserializer extends JsonDeserializer<VendingMachineEntity> {

	@Override
	public VendingMachineEntity deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		System.out.println("Deserializing Vending machine object");
		JsonNode jNode = p.readValueAsTree();
		VendingMachineEntity entity = new VendingMachineEntity();
		System.out.println(jNode.toString());
		entity.setName(jNode.get("name").asText());
		JsonParser bankParser = jNode.get("bank").traverse();
		bankParser.setCodec(p.getCodec());
		VendingBankEntity bank = bankParser.readValuesAs(VendingBankEntity.class).next();
		entity.setBank(bank);
		return entity;
	}

}

final class VendingMachineEntitySerializer extends JsonSerializer<VendingMachineEntity> {

	@Override
	public void serialize(VendingMachineEntity value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
		gen.writeStartObject();
		gen.writeObjectField("name", value.getName());
		gen.writeObjectField("bank", value.getBank());
		gen.writeEndObject();
		
	}
	
}



