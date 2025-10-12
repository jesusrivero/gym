# 🏋️‍♂️Gym Control

[![Kotlin](https://img.shields.io/badge/Kotlin-FF5722?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Hilt](https://img.shields.io/badge/Hilt-0D47A1?style=for-the-badge&logo=android&logoColor=white)](https://dagger.dev/hilt/)

Gym Control es una aplicación Android profesional diseñada para la gestión integral de gimnasios y centros deportivos, desarrollada con Kotlin + Jetpack Compose siguiendo los principios de Clean Architecture.
Permite administrar clientes, membresías, pagos y promociones, con control de acceso por roles y notificaciones automáticas sobre el estado de las membresías.

La app integra Firebase para autenticación, base de datos en la nube y mensajería push, asegurando una experiencia fluida.

✨ Características principales

✅ Registro, edición y eliminación de clientes y membresías

✅ Administración de pagos, promociones y planes personalizados

✅ Inicio de sesión con roles (Dueño y Administrador)

✅ Restricción de funciones según el tipo de usuario

✅ Generación automática de reportes e historial de pagos en PDF

✅ Escaneo y generación de códigos QR para identificación rápida e suscripcion a un gym

✅ Sincronización con Firebase Firestore

✅ Notificaciones push automáticas para avisar vencimientos de membresías

✅ Modo claro/oscuro y diseño moderno basado en Material 3


🧩 Arquitectura

El proyecto está estructurado bajo el patrón Clean Architecture, dividiendo las responsabilidades en tres capas principales:

Capa	Descripción

🧠 Domain	Casos de uso, modelos y lógica de negocio

💾 Data	Repositorios, Firebase, Room y fuentes locales/remotas

🎨 Presentation	Pantallas en Jetpack Compose, ViewModels e inyección con Hilt

🧱 Tecnologías utilizadas

Componente	Descripción

🧱 Jetpack Compose	UI moderna y declarativa

🧠 Hilt (Dagger)	Inyección de dependencias

☁️ Firebase Authentication & Firestore	Autenticación y sincronización en la nube

💾 Room Database	Almacenamiento local y cacheo offline

📄 PDF Generator	Creación de reportes y facturas en PDF

🧩 QR Code Integration	Identificación de usuarios mediante códigos QR

🧭 Navigation Compose	Flujo de navegación moderno

🧼 Clean Architecture	Separación por capas y principios SOLID

🚀 Futuras mejoras

📈 Dashboard con estadísticas de membresías y pagos

📲 Panel web administrativo conectado a Firebase

🧩 Personalización de facturas y reportes

🧠 Sistema de inteligencia para sugerir promociones automáticas
