package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/guests")
public class GuestController extends BaseRestController {

    private GuestService guestService;

    @PostMapping
    public ResponseEntity<Void> addGuest(@Valid @RequestBody CreateOrUpdateGuestRequestDTO createOrUpdateGuestRequestDTO) {
        return ResponseEntity.created(locationByEntity(guestService.addGuest(createOrUpdateGuestRequestDTO).getId())).build();
    }

    @PutMapping(value = "/{guestId}")
    public ResponseEntity<GuestDTO> updateGuest(@Valid @RequestBody CreateOrUpdateGuestRequestDTO createOrUpdateGuestRequestDTO,
                                                @PathVariable Long guestId) {
        return ResponseEntity.ok(guestService.updateGuest(createOrUpdateGuestRequestDTO, guestId));
    }

    @DeleteMapping(value = "/{guestId}")
    public ResponseEntity<Void> removeGuest(@PathVariable Long guestId){
        guestService.removeGuest(guestId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{guestId}")
    public ResponseEntity<GuestDTO> findGuestById(@PathVariable Long guestId){
        return ResponseEntity.ok(guestService.findGuestById(guestId));
    }

    @GetMapping
    public ResponseEntity<List<GuestDTO>> findAllGuests(){
        return ResponseEntity.ok(guestService.findAllGuests());
    }

    @GetMapping("/search")
    public ResponseEntity<List<GuestDTO>> findGuestByName(@RequestParam String name){
        return ResponseEntity.ok(guestService.findGuestsByName(name));
    }

}
