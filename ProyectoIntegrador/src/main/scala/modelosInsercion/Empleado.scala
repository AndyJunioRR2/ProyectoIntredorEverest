package modelosInsercion

case class Empleado(
                     id_empleado: String,        // ID del empleado
                     nombre: String,          // Nombre del empleado
                     departamento: Option[Int],  // Departamento del empleado (opcional)
                     job: Option[ Int],           // Cargo del empleado (opcional)
                     genero: Option[String],        // Género del empleado (opcional)
                     ruta_del_perfil: Option[String] // Ruta del perfil (opcional)
                   )

// Puede ser NULL

