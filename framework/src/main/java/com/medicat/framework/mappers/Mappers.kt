package com.medicat.framework.mappers
import com.medicat.domain.Elemento
import com.medicat.domain.Materia
import com.medicat.domain.NotificacionItem
import com.medicat.domain.Recuperatorio
import com.medicat.domain.Saber
import com.medicat.framework.dto.ElementoDto
import com.medicat.framework.dto.MateriaDto
import com.medicat.framework.dto.NotificacionDto
import com.medicat.framework.dto.RecuperatorioDto
import com.medicat.framework.dto.SaberDto

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

fun NotificacionDto.toModel(): NotificacionItem =
    NotificacionItem(
        id = id,
        userId = userId,
        competitionId = competitionId,
        type = type,
        title = title,
        message = message,
        isRead = isRead,
        sentAt = sentAt,
    )
