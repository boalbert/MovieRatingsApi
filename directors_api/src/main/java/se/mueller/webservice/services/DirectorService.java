package se.mueller.webservice.services;
import org.springframework.data.jpa.domain.Specification;
import se.mueller.webservice.dtos.Directordto;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import se.mueller.webservice.dtos.DirectorNationality;
import se.mueller.webservice.entities.DirectorSpecifications;
import se.mueller.webservice.entities.SearchCriteria;
import se.mueller.webservice.mappers.DirectorMapper;
import se.mueller.webservice.repositories.DirectorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DirectorService implements se.mueller.webservice.services.Service {

    private final DirectorRepository directorRepository;
    private DirectorMapper directorMapper;

    public DirectorService(DirectorRepository directorRepository, DirectorMapper directorMapper) {
        this.directorRepository = directorRepository;
        this.directorMapper = directorMapper;
    }

    @Override
    public List<Directordto> getAllDirectors() {
        return directorMapper.mapp(directorRepository.findAll());
    }

    @Override
    public Optional<Directordto> getOne(Long id) {

        return directorMapper.mapp(directorRepository.findById(id));
    }

    @Override
    public Directordto createDirector(Directordto directorDto) {
        if (directorDto.getFirstName().isEmpty())
            throw new RuntimeException();
        return directorMapper.mapp(directorRepository.save(directorMapper.mapp(directorDto)));
    }

    @Override
    public void delete(Long id) {
        directorRepository.deleteById(id);

    }

    @Override
    public Directordto replace(Long id, Directordto directorDto) {
        Optional<se.mueller.webservice.entities.Director> director = directorRepository.findById(id);
        if (director.isPresent()) {

            se.mueller.webservice.entities.Director updatedDirector = director.get();
            updatedDirector.setFirstName(directorDto.getFirstName());
            updatedDirector.setLastName(directorDto.getLastName());
            updatedDirector.setNationality(directorDto.getNationality());
            updatedDirector.setYearOfBirth(directorDto.getYearOfBirth());

            return directorMapper.mapp(directorRepository.save(updatedDirector));

        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Id " + id + " not found.");
    }

    @Override
    public Directordto update(Long id, DirectorNationality nationality) {
        Optional<se.mueller.webservice.entities.Director> director1 = directorRepository.findById(id);
        if (director1.isPresent()) {

            se.mueller.webservice.entities.Director updatedDirector = director1.get();
            if (nationality != null)
                updatedDirector.setNationality(nationality.nationality);
                return directorMapper.mapp(directorRepository.save(updatedDirector));

        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,  "Id " + id + " not found.");
    }

    @Override
    public List<Directordto> findAllBySpec(String search) {

       DirectorSpecifications spec1 =
             new DirectorSpecifications(new SearchCriteria("firstName",":",search));

            return directorRepository.findAll(Specification.where(spec1));
    }


}





