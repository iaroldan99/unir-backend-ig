# 📱 Instagram Messaging API - Integración de Mensajería Unificada

API REST desarrollada con **Java + Spring Boot** para integrar Instagram Messaging dentro de una plataforma de mensajería unificada.

## 🎯 Características

- ✅ **Recepción de mensajes** mediante webhooks de Instagram Graph API
- ✅ **Envío de mensajes** a usuarios de Instagram
- ✅ **Normalización de mensajes** a formato estándar (preparado para unificar con WhatsApp y Gmail)
- ✅ **Validación de webhooks** con verify token
- ✅ **Soporte para múltiples tipos de mensajes** (texto, imágenes, videos, etc.)

## 🏗️ Arquitectura

```
┌─────────────────┐
│   Instagram     │
│   (Meta API)    │
└────────┬────────┘
         │
         │ Webhooks / API Calls
         │
┌────────▼────────┐
│  Spring Boot    │
│     API         │
├─────────────────┤
│  - WebhookCtrl  │ ← Recibe mensajes
│  - MessageCtrl  │ ← Envía mensajes
│  - Services     │
│  - Models       │
└─────────────────┘
```

## 📋 Requisitos Previos

- **Java 17+**
- **Maven 3.8+**
- **Cuenta de Meta Developer**
- **Página de Facebook vinculada a Instagram Business Account**

## 🚀 Instalación y Configuración

### 1. Clonar y compilar el proyecto

```bash
mvn clean install
```

### 2. Configurar variables de entorno

Crea un archivo `.env` o exporta las siguientes variables:

```bash
export INSTAGRAM_ACCESS_TOKEN="tu_page_access_token_aqui"
export INSTAGRAM_VERIFY_TOKEN="tu_token_de_verificacion_secreto"
export INSTAGRAM_PAGE_ID="tu_instagram_business_account_id"
```

### 3. Configurar la aplicación de Meta (Instagram)

#### Paso 1: Crear una App en Meta for Developers

1. Ve a [Meta for Developers](https://developers.facebook.com/)
2. Crea una nueva app o usa una existente
3. Agrega el producto **"Instagram"**
4. En la configuración de Instagram, ve a **"Basic Display"** o **"Messenger API"**

#### Paso 2: Obtener el Access Token

1. Ve a **Tools** → **Graph API Explorer**
2. Selecciona tu app
3. Selecciona tu página de Facebook vinculada a Instagram
4. Genera un **Page Access Token** con los permisos:
   - `instagram_basic`
   - `instagram_manage_messages`
   - `pages_messaging`
   - `pages_manage_metadata`

5. **IMPORTANTE**: Convierte el token temporal a un token de larga duración:

```bash
curl -i -X GET "https://graph.facebook.com/v18.0/oauth/access_token?grant_type=fb_exchange_token&client_id=TU_APP_ID&client_secret=TU_APP_SECRET&fb_exchange_token=TU_TOKEN_TEMPORAL"
```

#### Paso 3: Obtener el Instagram Business Account ID

```bash
curl -i -X GET "https://graph.facebook.com/v18.0/me/accounts?access_token=TU_PAGE_ACCESS_TOKEN"
```

Luego obtén el Instagram Account ID:

```bash
curl -i -X GET "https://graph.facebook.com/v18.0/TU_PAGE_ID?fields=instagram_business_account&access_token=TU_ACCESS_TOKEN"
```

#### Paso 4: Configurar Webhook

1. En tu app de Meta, ve a **Products** → **Webhooks**
2. Haz clic en **"Configure Webhooks"** para Instagram
3. Configura el webhook:
   - **Callback URL**: `https://tu-dominio.com/webhook` (debe ser HTTPS en producción)
   - **Verify Token**: El mismo que configuraste en `INSTAGRAM_VERIFY_TOKEN`
4. Suscríbete a los siguientes eventos:
   - `messages`
   - `messaging_postbacks` (opcional)
   - `message_echoes` (opcional)

> **Nota para desarrollo local**: Usa [ngrok](https://ngrok.com/) o similar para exponer tu servidor local:
> ```bash
> ngrok http 8080
> ```
> Usa la URL HTTPS que te da ngrok como Callback URL.

### 4. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## 📡 Endpoints Disponibles

### 1. Webhook de Instagram (Recepción de mensajes)

#### GET `/webhook` - Verificación del webhook

Instagram lo usa para verificar tu webhook.

**Parámetros de query:**
- `hub.mode`: "subscribe"
- `hub.verify_token`: Tu token de verificación
- `hub.challenge`: Challenge que debes retornar

**Respuesta exitosa:** Retorna el `hub.challenge`

#### POST `/webhook` - Recibir mensajes

Instagram envía mensajes a este endpoint.

**Ejemplo de payload recibido:**

```json
{
  "object": "instagram",
  "entry": [
    {
      "id": "instagram-business-account-id",
      "time": 1694564800,
      "messaging": [
        {
          "sender": {
            "id": "sender-instagram-user-id"
          },
          "recipient": {
            "id": "tu-instagram-business-account-id"
          },
          "timestamp": 1694564800000,
          "message": {
            "mid": "message-id",
            "text": "Hola, ¿cómo están?"
          }
        }
      ]
    }
  ]
}
```

**Respuesta:** `200 OK` con body `"EVENT_RECEIVED"`

### 2. Envío de Mensajes

#### POST `/api/messages/send` - Enviar mensaje de texto

Envía un mensaje de texto a un usuario de Instagram.

**Request:**

```bash
curl -X POST http://localhost:8080/api/messages/send \
  -H "Content-Type: application/json" \
  -d '{
    "recipientId": "instagram_user_id",
    "text": "¡Hola desde nuestra API de mensajería unificada!"
  }'
```

**Respuesta exitosa:**

```json
{
  "status": "success",
  "message": "Mensaje enviado correctamente",
  "response": "{\"recipient_id\":\"...\",\"message_id\":\"...\"}"
}
```

**Respuesta de error:**

```json
{
  "status": "error",
  "message": "Descripción del error"
}
```

### 3. Health Checks

#### GET `/webhook/health`

```bash
curl http://localhost:8080/webhook/health
```

**Respuesta:** `"Webhook activo y funcionando"`

#### GET `/api/messages/health`

```bash
curl http://localhost:8080/api/messages/health
```

**Respuesta:**

```json
{
  "status": "UP",
  "service": "Message Controller"
}
```

## 🧪 Ejemplos de Uso con cURL

### Probar el envío de mensajes

```bash
# Enviar un mensaje simple
curl -X POST http://localhost:8080/api/messages/send \
  -H "Content-Type: application/json" \
  -d '{
    "recipientId": "7234567890123456",
    "text": "Este es un mensaje de prueba desde nuestra API"
  }'
```

### Simular un webhook de Instagram (para testing local)

```bash
curl -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{
    "object": "instagram",
    "entry": [
      {
        "id": "123456789",
        "time": 1694564800,
        "messaging": [
          {
            "sender": { "id": "sender-id-123" },
            "recipient": { "id": "recipient-id-456" },
            "timestamp": 1694564800000,
            "message": {
              "mid": "msg-id-789",
              "text": "Hola, este es un mensaje de prueba"
            }
          }
        ]
      }
    ]
  }'
```

## 📊 Estructura del Proyecto

```
src/main/java/com/uade/seminario/
├── InstagramMessagingApplication.java    # Clase principal
├── config/
│   ├── InstagramConfig.java              # Configuración de Instagram API
│   └── WebClientConfig.java              # Cliente HTTP
├── controller/
│   ├── WebhookController.java            # Recepción de webhooks
│   └── MessageController.java            # Envío de mensajes
├── service/
│   ├── MessageProcessorService.java      # Procesa mensajes entrantes
│   └── InstagramMessageSenderService.java # Envía mensajes salientes
└── model/
    ├── IncomingMessage.java              # Modelo de mensajes entrantes
    ├── OutgoingMessage.java              # Modelo de mensajes salientes
    └── MessageDTO.java                   # DTO normalizado
```

## 🔄 Flujo de Datos

### Recepción de Mensajes (Webhook)

1. Usuario envía mensaje en Instagram
2. Instagram envía webhook a `/webhook` (POST)
3. `WebhookController` valida y recibe el evento
4. `MessageProcessorService` procesa y normaliza el mensaje
5. El mensaje se convierte a `MessageDTO` (formato estándar)
6. Se registra en logs (próximamente: guardar en BD o enviar a cola)

### Envío de Mensajes

1. Cliente hace POST a `/api/messages/send`
2. `MessageController` valida el request
3. `InstagramMessageSenderService` construye el mensaje
4. Se envía a Instagram Graph API usando WebClient
5. Se retorna la respuesta al cliente

## 🔐 Seguridad

- ✅ **Validación de verify token** en webhooks
- ✅ **Access token** almacenado en variables de entorno
- ⚠️ **HTTPS requerido** en producción para webhooks
- ⚠️ **Rate limiting** de Instagram: 100 mensajes/hora por usuario

## 🐛 Debugging

### Verificar que el webhook está funcionando

```bash
# Health check
curl http://localhost:8080/webhook/health

# Ver logs en tiempo real
mvn spring-boot:run
```

### Problemas comunes

**1. Webhook no se verifica en Meta:**
- Verifica que la URL sea HTTPS (usa ngrok en desarrollo)
- Asegúrate que el verify token coincida
- Revisa los logs del servidor

**2. No se reciben mensajes:**
- Verifica que estés suscrito a los eventos correctos en Meta
- Verifica que el webhook esté activo
- Revisa los logs de Meta Developer Console

**3. Error al enviar mensajes:**
- Verifica que el access token sea válido y de larga duración
- Verifica que tengas los permisos necesarios
- Verifica que el recipientId sea correcto

## 📚 Documentación

Toda la documentación está organizada en la carpeta [`docs/`](docs/):

- **[INDEX.md](docs/INDEX.md)** - Índice completo de documentación
- **[QUICK_START.md](docs/QUICK_START.md)** - Inicio rápido (< 10 min)
- **[SETUP_GUIDE.md](docs/SETUP_GUIDE.md)** - Guía de configuración detallada
- **[CREDENCIALES_INSTAGRAM.md](docs/CREDENCIALES_INSTAGRAM.md)** - Cómo obtener credenciales
- **[CURL_COMMANDS.md](docs/CURL_COMMANDS.md)** - Todos los comandos cURL
- **[PASOS_CONFIGURACION.md](docs/PASOS_CONFIGURACION.md)** - Checklist de configuración
- **[GIT_CONFIG.md](docs/GIT_CONFIG.md)** - Configuración de Git

### Scripts Disponibles

- **[test-api.sh](scripts/test-api.sh)** - Suite de tests automatizados
- **[curl-examples.sh](scripts/curl-examples.sh)** - Ejemplos detallados
- **[setup-credentials.sh](scripts/setup-credentials.sh)** - Configurar credenciales

### Colección de Postman

Importa [`postman/Instagram_API.postman_collection.json`](postman/Instagram_API.postman_collection.json) en Postman para probar todos los endpoints.

## 🚀 Próximos Pasos

Para integrar con el sistema de mensajería unificada:

1. **Agregar persistencia**: Guardar mensajes en base de datos
2. **Sistema de colas**: Usar RabbitMQ o Kafka para procesar mensajes
3. **API unificada**: Crear capa de abstracción sobre Instagram/WhatsApp/Gmail
4. **Webhooks centralizados**: Router para diferentes plataformas
5. **Autenticación**: Agregar JWT o OAuth2 para proteger los endpoints

## 📚 Recursos Útiles

- [Instagram Messaging API - Meta Developers](https://developers.facebook.com/docs/messenger-platform/instagram)
- [Instagram Graph API Reference](https://developers.facebook.com/docs/instagram-api)
- [Webhook Best Practices](https://developers.facebook.com/docs/graph-api/webhooks/getting-started)

---

**¿Necesitas ayuda?** Revisa los logs con `mvn spring-boot:run` o consulta la documentación de Meta Developers.


