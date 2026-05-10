# Proyecto Final · Programación · 1º DAW

Aplicación Java para la gestión de películas, actores y directores.  
Proyecto final del módulo de Programación de 1º DAW.

---

# Descripción del Proyecto

La aplicación permitirá gestionar:

- Películas
- Actores
- Directores
- Usuarios
- Listas generales y personales

El sistema funcionará mediante menús por consola y almacenará la información en ficheros para mantener los datos entre ejecuciones.

---

# Estructura de Clases

## Clases Principales

### Actor

Clase encargada de representar a un actor.

---

### Director

Clase encargada de representar a un director.

---

### Película

#### Atributos necesarios

- `titulo`
- `anyo`
- `duracion`

---

### Usuario (Clase Abstracta)

#### Atributos necesarios

- `nombre`
- `apellidos`
- `correo`
- `poblacion`
- `fechaNacimiento`
- `contrasenya`

#### Tipos de usuario

- `UsuarioNormal`
- `UsuarioAdministrador`

---

# Sistema de Listas

## Lista General

Lista compartida por todos los usuarios.

### Funcionalidades

- Cualquier usuario puede añadir elementos.
- Solo los administradores pueden eliminar elementos.

---

## Lista Personal

Cada usuario tendrá sus propias listas personales.

### Características

- Se construyen a partir de elementos de la lista general.
- El usuario puede:
  - añadir elementos
  - eliminar elementos
  - ordenar listas según distintos criterios

---

# Inicio de la Aplicación

Al iniciar el programa aparecerán dos opciones:

- Login
- Registro

---

## Login

Se solicitarán los siguientes datos:

- Nombre de usuario
- Contraseña
- Confirmación de contraseña

### Funcionamiento

- Si los datos son correctos:
  - se accederá a la aplicación.
- Si los datos son incorrectos:
  - se mostrarán alertas de error.

---

## Registro

Para crear un nuevo usuario se solicitarán todos los atributos necesarios:

- Nombre
- Apellidos
- Correo
- Población
- Fecha de nacimiento
- Contraseña
- Confirmación de contraseña

Una vez completado el proceso, el usuario quedará registrado en el sistema.

---

# Gestión de Usuarios Administradores

Se plantean dos posibles enfoques:

## Opción 1

Permitir elegir el tipo de usuario durante el registro:

- Usuario normal
- Usuario administrador

---

## Opción 2

Crear un administrador por defecto en el sistema.

### Funcionamiento

- Todos los nuevos usuarios serán normales.
- Los administradores podrán ascender usuarios normales a administradores.

---

# Menú Principal

Una vez dentro de la aplicación, el usuario podrá:

- Consultar catálogos generales:
  - películas
  - actores
  - directores

- Añadir nuevos elementos:
  - películas
  - actores
  - directores

- Construir listas personales

- Eliminar elementos de listas personales

- Consultar y ordenar listas personales según distintos criterios:
  - título
  - año
  - duración

- Ver información detallada de cada elemento

---

# Tecnologías Utilizadas

- Java
- Programación orientada a objetos
- Git
- GitHub
- Persistencia mediante ficheros

---

# Trabajo en Equipo

El proyecto será desarrollado en grupos de 3 personas utilizando un repositorio compartido en GitHub.

Cada integrante será responsable de una parte concreta del proyecto.

---

# Posibles Entregas del Proyecto

| Entrega | Contenido |
|---|---|
| 1 | Estructura inicial y diagrama de clases |
| 2 | Login y gestión básica |
| 3 | Eliminación y control de duplicados |
| 4 | Interfaces y ordenaciones |

---

# Objetivos del Proyecto

- Aplicar programación orientada a objetos
- Practicar trabajo en equipo
- Utilizar Git y GitHub
- Gestionar persistencia de datos
- Diseñar aplicaciones Java modulares

---

