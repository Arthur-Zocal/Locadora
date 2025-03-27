package com.locadora.services;

import com.locadora.domains.Carro2;
import com.locadora.domains.dtos.Carro2DTO;
import com.locadora.repositories.Carro2Repository;
import com.locadora.services.exceptions.DataIntegrityViolationException;
import com.locadora.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class Carro2Service {


    @Autowired
    private Carro2Repository carroRepo2;

    public List<Carro2DTO> findAll(){
        return carroRepo2.findAll().stream()
                .map(obj -> new Carro2DTO(obj))
                .collect(Collectors.toList());
    }

    public Carro2 findById(Integer id){
        Optional<Carro2> obj = carroRepo2.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("veiculo nao encontrado id: "+ id));
    }

    public Carro2 findByCpf(String cpfProprietario){
        Optional<Carro2> obj = carroRepo2.findByCpfProprietario(cpfProprietario);
        return obj.orElseThrow(() -> new ObjectNotFoundException("cpf nao foi encontrado  cpf: "+ cpfProprietario));
    }

    public Carro2 create (Carro2DTO dto){
        dto.setIdCarro2(null);
        validaCarro(dto);
        Carro2 obj = new Carro2(dto);
        return carroRepo2.save(obj);
    }

    private void validaCarro(Carro2DTO dto){
        Optional<Carro2> objCpf = carroRepo2.findByCpfProprietario(dto.getCpfProprietario());
        if (objCpf.isPresent() && objCpf.get().getIdCarro2() != dto.getIdCarro2()){
            throw new DataIntegrityViolationException("carro ja esta cadastrado");
        }
        if(!carroRepo2.existsById(dto.getIdCarro2())){
            throw new DataIntegrityViolationException("carro do proprietario nao enconrtrado id "+dto.getIdCarro2());
        }
    }

    public Carro2 update(Integer id, Carro2DTO objDto){
        objDto.setIdCarro2(id);
        Carro2 oldObj = findById(id);
        validaCarro(objDto);
        oldObj = new Carro2(objDto);
        return carroRepo2.save(oldObj);
    }

    public void delete(Integer id){
        Carro2 obj = findById(id);
        carroRepo2.deleteById(id);
    }
}

