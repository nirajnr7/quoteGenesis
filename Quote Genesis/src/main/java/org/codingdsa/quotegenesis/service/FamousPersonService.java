package org.codingdsa.quotegenesis.service;

import org.codingdsa.quotegenesis.dto.FamousPersonDto;
import org.codingdsa.quotegenesis.model.FamousPerson;
import org.codingdsa.quotegenesis.repository.FamousPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FamousPersonService {
    @Autowired
    private FamousPersonRepository famousPersonRepository;

    public FamousPerson addFamousPerson(FamousPersonDto famousPersonDto) {
        FamousPerson famousPerson = new FamousPerson();
        // Set properties from famousPersonDto to famousPerson
        famousPerson.setName(famousPersonDto.getName());
        // Add other properties as needed
        return famousPersonRepository.save(famousPerson);
    }
}
