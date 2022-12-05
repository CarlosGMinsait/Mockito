package com.minsait.mockito.services;

import com.minsait.mockito.models.Examen;
import com.minsait.mockito.repositories.ExamenRepository;
import com.minsait.mockito.repositories.PreguntaRepository;

import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl  implements ExamenService{

    ExamenRepository examenRepository;
    PreguntaRepository preguntaRepository;
    @Override
    public Optional<Examen> findExamenProNombre(String nombre) {
        examenRepository.findAll();
        return examenRepository.findAll().stream()
               // .filter(examen -> examen.equals(nombre))
                .filter((examen -> examen.getNombre().equals(nombre)))
                .findFirst();

    }

    @Override
    public Examen findExamenPorNombreConPreguntas(String nombre) {
        Optional<Examen>examen=findExamenProNombre(nombre);
        if(examen.isPresent()){
            examen.get().setPreguntas(preguntaRepository
                    .findPreguntasByExamenId(examen.get().getId()));
            return examen.get();
        }
        return null;
    }

    @Override
    public Examen save(Examen examen) {
        //validar si tiene preguntas si tiene preguntas guardar el examen con preguntas
        //sino guardar solo el examen
        if(!examen.getPreguntas().isEmpty()){
            preguntaRepository.savePreguntas(examen.getPreguntas());
        }
        return  examenRepository.save(examen);
    }

}



























