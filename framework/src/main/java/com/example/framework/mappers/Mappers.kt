package com.example.framework.mappers
import com.example.domain.Materia
import com.example.framework.dto.MateriaDto

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
