package com.minsait.mockito.services;

import com.minsait.mockito.models.Examen;
import com.minsait.mockito.repositories.ExamenRepository;
import com.minsait.mockito.repositories.PreguntaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

    @Mock
    ExamenRepository examenRepository;

    @Mock
    PreguntaRepository preguntaRepository;

    @InjectMocks
    ExamenServiceImpl service;

    @Captor
    ArgumentCaptor<Long> captor;

    @Test
    void testArgumentCaptor(){
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenPorNombreConPreguntas("Historia");

        verify(preguntaRepository).findPreguntasByExamenId(captor.capture());
        assertEquals(3L,captor.getValue());

    }


    @Test
    void testFindExamenProNombre() {
        //Datos necesario simulados
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        //llamada al metodo
        Optional<Examen> examen = service.findExamenProNombre("Matematicas");

        //Prueba unitaria o la afirmacion de que nuestro examen se ha obtenido correctamente

        assertTrue(examen.isPresent());
    }
    @Test
    void testPreguntasDeExamen(){
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        assertTrue(examen.getPreguntas().contains("Aritmetica"));

        verify(examenRepository,atLeast(2)).findAll(); //times, atmost, atmostone, atleastonce, atleast
        verify(preguntaRepository).findPreguntasByExamenId(anyLong());
    }


    @Test
    void testException(){
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class,()->{
            service.findExamenPorNombreConPreguntas("Matematicas");
            });

        assertTrue(IllegalArgumentException.class.equals(exception.getClass()));
    }
    @Test
    void testDoTrhow(){
        doThrow(IllegalArgumentException.class).when(preguntaRepository).savePreguntas(anyList());
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);

        assertThrows(IllegalArgumentException.class, () -> service.save(examen));
    }

    @Test
    void testDoAnswer(){
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        //when(preguntaRepository.findPreguntasByExamenId(1L)).thenReturn(Datos.PREGUNTAS);
        //when(preguntaRepository.findPreguntasByExamenId(2L)).thenReturn(Datos.PREGUNTAS);
        doAnswer(invocationOnMock -> {
            Long id = invocationOnMock.getArgument(0);
            return id == 1 ? Datos.PREGUNTAS : Collections.EMPTY_LIST;
        }).when(preguntaRepository).findPreguntasByExamenId(anyLong());

        Examen examen = service.findExamenPorNombreConPreguntas("Español");

        assertAll(
                () -> assertNotNull(examen),
                () -> assertTrue(examen.getPreguntas().isEmpty()),
                () -> assertEquals(0,examen.getPreguntas().size())
        );


    }
    @Test
    void testNombreNull(){
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        Examen examen = service.findExamenPorNombreConPreguntas("Programacion");
        assertNull(examen);
    }
    @Test
    void testSave(){
        when(examenRepository.save(Datos.EXAMEN)).thenReturn(Datos.EXAMEN);
        Examen examen = service.save(Datos.EXAMEN);
        assertAll(
                () -> assertEquals(4L,examen.getId()),
                () -> assertEquals("Quimica",examen.getNombre()),
                () -> assertTrue(examen.getPreguntas().isEmpty())
        );



    }

        }