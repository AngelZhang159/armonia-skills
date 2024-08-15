# Armonía Skills
## Aplicación Android

**Armonía Skills** es una aplicación para Android que permite a los usuarios crear servicios (llamados "skills") y a otros usuarios contratarlos. Es una plataforma que facilita la conexión entre quienes ofrecen y quienes buscan servicios especializados.

## Tabla de Contenidos

- [Descripción del proyecto](#descripción-del-proyecto)
- [Funcionalidades principales](#funcionalidades-principales)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Autenticación](#autenticación)
- [Chat en Tiempo Real](#chat-en-tiempo-real)
- [Backend](#backend)

## Descripción del Proyecto

![imagen](https://github.com/user-attachments/assets/5d6063e1-7794-4b0c-8db2-31d255e0a77c)


Armonía Skills se basa en una arquitectura cliente-servidor, donde la aplicación Android interactúa con un backend en SpringBoot a través de una API REST que permite operaciones CRUD. Esta solución facilita la creación y gestión de servicios por parte de los usuarios y su contratación por otros.

## Funcionalidades Principales

- **Creación y Gestión de Skills:** Los usuarios pueden crear y administrar sus propios servicios.
- **Contratación de Skills:** Otros usuarios pueden buscar, contratar y calificar estos servicios.
- **Chat en Tiempo Real:** Comunicación instantánea entre usuarios mediante WebSockets.
- **Autenticación Segura:** Uso de JWT para asegurar el acceso a la aplicación.

## Requisitos

1. **Android Studio**: Entorno de desarrollo para construir la aplicación.
2. **Backend**: Necesitas el [backend de Armonía Skills](https://github.com/AngelZhang159/armonia-skills-back) desplegado para probar todas las funcionalidades.
3. **Dispositivo Android** (Opcional): Para probar la aplicación en un dispositivo físico.

## Instalación

1. **Clona el Proyecto**: Descarga el código fuente del repositorio y ábrelo en Android Studio.
2. **Configura el Backend**: Si has iniciado el backend, realiza un "Replace All" de la IP del servidor (13.38.130.77) por tu propia IP, ya que la IP actual no está activa.
3. **Ejecuta la Aplicación**: Haz clic en "Run" en Android Studio para ejecutar la aplicación en un emulador o dispositivo físico.
4. **Dispositivo Físico** (Opcional): Si deseas usar un dispositivo Android, activa la depuración USB en los ajustes de desarrollador y conéctalo a tu computadora mediante USB (también es posible la conexión por Wi-Fi). Luego, haz clic en "Run" en Android Studio.

## Autenticación

La aplicación utiliza JWT (JSON Web Token) para la autenticación de usuarios. Al registrarse, se genera un ID único para el usuario. En el inicio de sesión, el servidor valida las credenciales del usuario y, si son correctas, genera un JWT que incluye el ID único del usuario. Este token es almacenado en las Shared Preferences del dispositivo y debe ser incluido en las cabeceras HTTP para todas las solicitudes posteriores. Si el token está ausente o es inválido, el servidor responderá con un error 401 UNAUTHORIZED y se redirigirá al usuario a la pantalla de inicio de sesión.

## Chat en Tiempo Real

![imagen](https://github.com/user-attachments/assets/16f92b94-ba31-499b-973b-6476481ee5ac)

La aplicación ofrece una funcionalidad de chat en tiempo real mediante WebSockets. Aquí te explicamos cómo funciona:

- **Conexión**: Al abrir el chat, se establece una conexión WebSocket con el servidor. El servidor guarda el ID de la conexión asociado al usuario.
- **Envío de Mensajes**: Cuando un usuario envía un mensaje, el servidor lo intercepta, lo guarda en la base de datos y verifica si el destinatario tiene una conexión activa. Si es así, el mensaje se reenvía instantáneamente. Si el destinatario no está conectado, el mensaje se envía a través de Firebase Cloud Messaging (FCM), permitiendo que el destinatario lo reciba incluso si la aplicación está cerrada.

## Backend

El backend de Armonía Skills está disponible en un repositorio separado. Puedes consultar el código y más detalles en el [repositorio del backend](https://github.com/AngelZhang159/armonia-skills-back).
