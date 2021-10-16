package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GuestService {

    private GuestRepository guestRepository;

    private GuestMapper guestMapper;

    public GuestDTO addGuest(CreateOrUpdateGuestRequestDTO createOrUpdateGuestRequestDTO){
        Guest guest = guestRepository.saveAndFlush(guestMapper.map(createOrUpdateGuestRequestDTO));

        return guestMapper.map(guest);
    }

    public GuestDTO updateGuest(CreateOrUpdateGuestRequestDTO createOrUpdateGuestRequestDTO, Long guestId) {
        return guestRepository.findById(guestId).map(guest -> {
            guestMapper.updateGuestFromDto(createOrUpdateGuestRequestDTO,guest);

            return guestMapper.map(guestRepository.saveAndFlush(guest));
        }).orElseThrow(() -> {
            throw new EntityNotFoundException("Guest not found.");
        });
    }

    public void removeGuest(Long id){
        Optional<Guest> guest = guestRepository.findById(id);

        if (!guest.isPresent()) throw new EntityNotFoundException("Guest not found.");

        guestRepository.delete(guest.get());
    }

    public GuestDTO findGuestById(Long id){
        return guestRepository.findById(id).map(guestMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Guest not found.");
        });
    }

    public List<GuestDTO> findAllGuests(){
        return guestMapper.map(guestRepository.findAll());
    }

    public List<GuestDTO> findGuestsByName(String name){
        return guestMapper.map(guestRepository.findByNameContainingIgnoreCase(name));
    }
}
