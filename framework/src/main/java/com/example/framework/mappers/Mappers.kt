package com.example.framework.mappers
import com.example.domain.Subject
import com.example.framework.dto.SubjectResponseDto

fun SubjectResponseDto.toModel(): Subject {
        return Subject(
        id= id,
        name= description,
        image= image,
        teacherId= teacher,
        elementosTotales= elemTotal,
        recTotales= recTotales,
        recTomados= recTomados,
        elemEvaluados= elemEvaluados,
        elemCompletes= elemComp
    )
}