package com.machine.vending.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.machine.vending.model.entity.CoinRegistryEntity;
import com.machine.vending.model.entity.VendingMachineEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="vending_machine", description="The Vending Monitoring API")
public interface VendingMachineChangeService {
	/**
	 * 
	 * 
	 * */
	@Operation(summary = "Get all the machines being monitored", description = "Get all the vending machines that are being monitored by the service.", tags= {"vending_machine"} )
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content= @Content(array= @ArraySchema(schema=@Schema(implementation=VendingMachineEntity.class))))
	})
	ResponseEntity<List<VendingMachineEntity>> getMachines();
	
	/**
	 * 
	 * */
	@Operation(summary= "Create a machine", 
			description = "To create a machine provide its name. When a vending machine is created a default coin registry is created for the machine."
					+ "To change the default coin registry use a separate api call ")
	@ApiResponses(value= {
			@ApiResponse(responseCode = "200", description = "successfully add a vending machine", content=@Content(schema=@Schema(implementation=VendingMachineEntity.class)))
	})
	ResponseEntity<VendingMachineEntity> addVendingMachine(String name);
	
	/**
	 * 
	 * */
	@Operation(summary="Update a machine coin registry",
			description = "A newly created machine will have a default coin registry. Use this api to update the coin registry.")
	@ApiResponses(value= {
			@ApiResponse(responseCode = "200", description="successfully updated vending machine coin registry", content = @Content(schema=@Schema(implementation=VendingMachineEntity.class))),
			@ApiResponse(responseCode = "404", description="The machine cannot be found", content = @Content(schema=@Schema(implementation=VendingMachineEntity.class))),
			@ApiResponse(responseCode = "400", description="If the machine name passed is invalid or request body of the updating registry is invalid ", content = @Content(schema=@Schema(implementation=CoinRegistryEntity.class)))
	})
	//@PutMapping(value="/machine/{name}/registry")
	ResponseEntity<VendingMachineEntity> updateCoinRegistry(String name, String type, CoinRegistryEntity registry);
	
	/**
	 * 
	 * 
	 * */
	@Operation(summary="Get a machine using it name",
			description = "Get a machine using its name, if not found please create one.")
	@ApiResponses(value= {
			@ApiResponse(responseCode = "200", description="The machine has been found", content = @Content(schema=@Schema(implementation=VendingMachineEntity.class))),
			@ApiResponse(responseCode = "404", description="The machine cannot be found", content = @Content(schema=@Schema(implementation=VendingMachineEntity.class))),
			@ApiResponse(responseCode = "400", description="The machine name passed is invalid", content = @Content(schema=@Schema(implementation=CoinRegistryEntity.class)))
	})

	ResponseEntity<VendingMachineEntity> getMachine(String name);
	
	
	/**
	 * 
	 * */
	@Operation(summary="Get the machine coin registry",
			description="Use the name of the machine to get the details about the coin registy.")
	@ApiResponses(value= {
			@ApiResponse(responseCode = "200", description="The machine coin registry has been obtained.", content = @Content(schema=@Schema(implementation=CoinRegistryEntity.class))),
			@ApiResponse(responseCode = "404", description="The machine cannot be found", content = @Content(schema=@Schema(implementation=CoinRegistryEntity.class))),
			@ApiResponse(responseCode = "400", description="The machine name passed is invalid", content = @Content(schema=@Schema(implementation=CoinRegistryEntity.class)))
	})
	ResponseEntity<CoinRegistryEntity> getMachineCoinRegistry(String name);
	
	/**
	 * 
	 * */
	@Operation(summary="Get the machine user coin registry", 
			description = "Use the name of the machine to get the details about the user coin registry")
	@ApiResponses(value= {
			@ApiResponse(responseCode = "200", description="The machine user coin registry has been obtained.", content = @Content(schema=@Schema(implementation=CoinRegistryEntity.class))),
			@ApiResponse(responseCode = "404", description="The machine cannot be found", content = @Content(schema=@Schema(implementation=CoinRegistryEntity.class))),
			@ApiResponse(responseCode = "400", description="The machine name passed is invalid", content = @Content(schema=@Schema(implementation=CoinRegistryEntity.class)))
	})
	ResponseEntity<CoinRegistryEntity> getUserCoinRegistry(String name);
	
	/**
	 * api function to update the payment made to the machine by the user to the monitor service.
	 * This update the machine and user coin registry using the payment made and also deduct the
	 * registry coin count based on the change provided.
	 * @Param name : machine name
	 * @Param amount : amount purchased
	 * @Param coins : coins provided to make the payment, checked by the client.
	 * @return returns vending machine state after the machine and user coin registry has been updated.
	 * */
	@Operation(summary="Get the change to be returned to the user.",
			description="Use the machine name to make the payment in coins for the amount purchased. The client already would have made sure the right payment is made.")
	@ApiResponses(value= {
			@ApiResponse(responseCode = "200", description="The machine has update the machine and user coin registry and return the change.", 
					content = @Content(schema=@Schema(implementation=CoinRegistryEntity.class))),
			@ApiResponse(responseCode = "404", description="The machine cannot be found", content = @Content(schema=@Schema(implementation=CoinRegistryEntity.class))),
			@ApiResponse(responseCode = "400", description="The parameter passed is invalid or payment amount is insufficient or not enough funds in machine registry.", content = @Content(schema=@Schema(implementation=CoinRegistryEntity.class)))
	})
	ResponseEntity<CoinRegistryEntity> submitPaymentAmount( String name,
			Double amount,
			CoinRegistryEntity coins);
}
