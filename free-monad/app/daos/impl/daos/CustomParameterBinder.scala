package daos.impl.daos

import models.AbstractId
import scalikejdbc.ParameterBinderFactory

trait CustomParameterBinder {
  implicit def longIdParameterBinderFactory[Id <: AbstractId[Long]]: ParameterBinderFactory[Id] =
    ParameterBinderFactory[Id] { v => (ps, idx) => ps.setLong(idx, v.unId) }
}
