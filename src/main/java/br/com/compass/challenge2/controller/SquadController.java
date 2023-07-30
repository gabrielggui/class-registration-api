package br.com.compass.challenge2.controller;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.compass.challenge2.entity.Squad;
import br.com.compass.challenge2.service.SquadService;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/squads")
public class SquadController {
	private final SquadService squadService;

	@Autowired
	public SquadController(SquadService squadService) {
		this.squadService = squadService;
	}
    
	
	@GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Squad>>> getAllSquads() {
        List<Squad> squads = squadService.findAll();
        List<EntityModel<Squad>> squadModels = EntityModel.of(squads).add(
                    linkTo(methodOn(SquadController.class).getAllSquads()).withSelfRel(),
                    linkTo(methodOn(SquadController.class).createSquad(null)).withRel("create")).toList();
            return ResponseEntity.status(HttpStatus.OK).body(CollectionModel.of(squadModels));
        }
    
	
	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Squad>> getSquadById(@PathVariable Long id) {		
	     	Squad squad = squadService.findById(id);
	    	if (squad != null) {
	    		 EntityModel<Squad> squadModel = EntityModel.of(squad).add(
	                     linkTo(methodOn(SquadController.class).getSquadById(id)).withSelfRel(),
	                     linkTo(methodOn(SquadController.class).getAllSquads()).withRel("all_squads"),
	                     linkTo(methodOn(SquadController.class).updateSquad(id, null)).withRel("update"),
	                     linkTo(methodOn(SquadController.class).deleteSquad(id)).withRel("delete"));
            return ResponseEntity.status(HttpStatus.OK).body(squadModel);
          } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
          }
	}
   	
	
	@PostMapping
	public ResponseEntity<?> createSquad(@RequestBody Squad squad) {
		 squad.setId(0L);
	        Squad createdSquad = squadService.save(squad);

	        EntityModel<Squad> squadModel = EntityModel.of(createdSquad).add(
	                linkTo(methodOn(SquadController.class).getSquadById(createdSquad.getId())).withSelfRel(),
	                linkTo(methodOn(SquadController.class).getAllSquads()).withRel("all_squads"),
	                linkTo(methodOn(SquadController.class).updateSquad(createdSquad.getId(), null)).withRel("update"),
	                linkTo(methodOn(SquadController.class).deleteSquad(createdSquad.getId())).withRel("delete")
	        );
	        return new ResponseEntity<>(createdSquad, HttpStatus.CREATED);
				
	}

	
	@PutMapping("/{id}")
	public ResponseEntity<EntityModel<Squad>> updateSquad(@PathVariable Long id, @Valid @RequestBody Squad squad) {
        squad.setId(id);
        Squad updatedSquad = squadService.update(id, squad);

        if (updatedSquad != null) {
            EntityModel<Squad> squadModel = EntityModel.of(updatedSquad).add(
                    linkTo(methodOn(SquadController.class).getSquadById(id)).withSelfRel(),
                    linkTo(methodOn(SquadController.class).getAllSquads()).withRel("all_squads")
            );

            return ResponseEntity.status(HttpStatus.OK).body(squadModel);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
	
	 @DeleteMapping("/{id}")
	    public ResponseEntity<EntityModel<Squad>> deleteSquad(@PathVariable Long id) {
	        Squad squad = squadService.findById(id);

	        if (squad != null) {
	            Squad deletedSquad = squadService.deleteById(id);
	            EntityModel<Squad> squadModel = EntityModel.of(deletedSquad).add(
	                    linkTo(methodOn(SquadController.class).getAllSquads()).withRel("all_squads")
	            );
	            return ResponseEntity.status(HttpStatus.OK).body(squadModel);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }
	    }
	
	
	
	@GetMapping("/search/squad-name")
	    public ResponseEntity<List<Squad>> findBySquadName(@RequestParam("name") String squadName) {
	        List<Squad> squads = squadService.findBySquadNameContainingIgnoreCase(squadName);
	        return ResponseEntity.status(HttpStatus.OK).body(squads);
	    }

     @GetMapping("/search/organizer-name")
	    public ResponseEntity<List<Squad>> findByOrganizerName(@RequestParam("name") String organizerName) {
	        List<Squad> squads = squadService.findByOrganizerNameContainingIgnoreCase(organizerName);
	        return ResponseEntity.status(HttpStatus.OK).body(squads);
	    }
	    
	
}
