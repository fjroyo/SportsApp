# SportsApp

Esta aplicación proporciona un servicio para que los usuarios creados puedan registrarse y asistir a eventos deportivos.

Consta de dos objetos de negocio:
- Eventos
- Usuarios

Un evento consta de las siguientes propiedades:
- Tipo de deporte
- Nombre del equipo 1
- Nombre del equipo 2
- Hora de inicio del evento
- Hora de fin del evento
- Ubicación del evento

Además, la aplicación permite buscar eventos por deporte, fecha de inicio y fin y ubicación.

Un usuario consta de las siguientes propiedades:
- Email
- Nombre
- Apellido
- Teléfono
- Posición

Además, una vez creado puede añadir sus intereses y recibirá una invitación para apuntarse a los eventos sobre los que ha manifestado interés. Una vez que un usuario se una a un evento, dicho evento registrará al usuario en su listado de jugadores del equipo correspondiente.

# Ejecutar la aplicación

Para ejecutar la aplicación, debe clonarse este repositorio e importarlo como proyecto Maven en el IDE preferido. 

A continuación debe editarse la clase src/main/java/com/mongodb/sports/model/MClient.java y configurar la variable CONNECTION_STRING al valor que se proporcionará durante el workshop (o en su defecto, a un clúster existente si el participante ya tiene uno).

Finalmente, se ejecutará como aplicación Java la clase src/main/java/com/mongodb/sports/SportsApp.java y se podrá acceder al interface gráfico conectádose a través de un navegedor a http://localhost:4567/

Nota: la aplicación no es completamente funcional. Falta el código correspondiente a añadir usuarios en la clase src/main/java/com/mongodb/sports/model/ManageUsers.java. Completar dicha clase es uno de los ejercicios del workshop.

