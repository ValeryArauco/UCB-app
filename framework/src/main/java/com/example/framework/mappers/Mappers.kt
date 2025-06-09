package com.example.framework.mappers
import com.example.domain.Elemento
import com.example.domain.Materia
import com.example.domain.Recuperatorio
import com.example.domain.Saber
import com.example.framework.dto.ElementoDto
import com.example.framework.dto.MateriaDto
import com.example.framework.dto.RecuperatorioDto
import com.example.framework.dto.SaberDto

fun MateriaDto.toModel(): Materia =
    Materia(
        id = id,
        name = name,
        image = image,
        docenteId = docenteId.toString(),
        paralelo = paralelo,
        elementosTotales = elementosTotales,
        recTotales = recTotales,
        recTomados = recTomados,
        elemEvaluados = elemEvaluados,
        elemCompletados = elemCompletados,
        sigla = sigla,
        gestion = gestion,
        vigente = vigente,
    )

fun ElementoDto.toModel(): Elemento =
    Elemento(
        id = id,
        materiaId = materiaId,
        descripcion = descripcion,
        fechaLimite = fechaLimite,
        fechaRegistro = fechaRegistro,
        saberesTotales = saberesTotales,
        saberesCompletados = saberesCompletados,
        completado = completado,
        evaluado = evaluado,
        comentario = comentario,
        fechaEvaluado = fechaEvaluado,
    )

fun SaberDto.toModel(): Saber =
    Saber(
        id = id,
        completado = completado,
        elementoCompetenciaId = elementoCompetenciaId,
        descripcion = descripcion,
    )

fun RecuperatorioDto.toModel(): Recuperatorio =
    Recuperatorio(
        id = id,
        completado = completado,
        elementoCompetenciaId = elementoCompetenciaId,
        fechaEvaluado = fechaEvaluado,
    )
