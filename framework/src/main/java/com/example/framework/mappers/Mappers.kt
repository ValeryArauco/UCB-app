package com.example.framework.mappers
import com.example.domain.Materia
import com.example.framework.dto.MateriaDto
import kotlin.io.path.fileVisitor

fun MateriaDto.toModel(): Materia {
        return Materia(
                id= id,
                name= fields.description.value,
                image= fields.image.value,
                docenteId = fields.teacher.value,
                elementosTotales= fields.elemTotal.value,
                recTotales= fields.recTotales.value,
                recTomados= fields.recTomados.value,
                elemEvaluados= fields.elemEvaluados.value,
                elemCompletados= fields.elemComp.value
    )
}