package com.example.framework.mappers
import com.example.domain.Materia
import com.example.framework.dto.MateriaResponseDto

fun MateriaResponseDto.toModel(): Materia {
        return Materia(
        id= id,
        name= description,
        image= image,
                docenteId = teacher,
        elementosTotales= elemTotal,
        recTotales= recTotales,
        recTomados= recTomados,
        elemEvaluados= elemEvaluados,
                elemCompletados= elemComp
    )
}