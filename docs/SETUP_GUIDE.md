# 🔧 Guía de Configuración - Instagram Messaging API

Esta guía te llevará paso a paso por el proceso de configuración de la integración con Instagram.

## 📝 Índice

1. [Prerequisitos](#prerequisitos)
2. [Configuración de Meta for Developers](#configuración-de-meta-for-developers)
3. [Obtener Credenciales](#obtener-credenciales)
4. [Configurar Webhooks](#configurar-webhooks)
5. [Configuración Local](#configuración-local)
6. [Probar la Integración](#probar-la-integración)

---

## 1️⃣ Prerequisitos

Antes de comenzar, asegúrate de tener:

- ✅ Una **cuenta de Facebook** (personal o de prueba)
- ✅ Una **página de Facebook** creada
- ✅ Una **cuenta de Instagram Business** vinculada a tu página de Facebook
- ✅ Acceso a **[Meta for Developers](https://developers.facebook.com/)**
- ✅ **Java 17+** y **Maven** instalados
- ✅ **ngrok** (para desarrollo local) - [Descargar aquí](https://ngrok.com/)

---

## 2️⃣ Configuración de Meta for Developers

### Paso 1: Crear una App en Meta

1. Ve a **[Meta for Developers](https://developers.facebook.com/)**
2. Haz clic en **"My Apps"** → **"Create App"**
3. Selecciona el tipo: **"Business"** o **"None"** (para desarrollo)
4. Completa los datos:
   - **App Display Name**: `Mi App de Mensajería` (o el nombre que prefieras)
   - **App Contact Email**: Tu email
   - **Business Account**: Selecciona o crea uno

5. Haz clic en **"Create App"**

### Paso 2: Agregar el Producto Instagram

1. En el dashboard de tu app, busca **"Add Product"**
2. Encuentra **"Instagram"** y haz clic en **"Set Up"**
3. También agrega **"Messenger"** si no está agregado

### Paso 3: Configurar Permisos

1. Ve a **"Settings"** → **"Basic"**
2. Agrega un **App Domain** (opcional para desarrollo)
3. Guarda los cambios

---

## 3️⃣ Obtener Credenciales

### A. Obtener el Page Access Token

1. Ve a **"Tools"** → **"Graph API Explorer"** en Meta for Developers
2. En el explorador:
   - Selecciona tu **App** en el dropdown
   - Haz clic en **"Generate Access Token"**
   - Selecciona tu **página de Facebook**
   - Autoriza los siguientes permisos:
     - ✅ `instagram_basic`
     - ✅ `instagram_manage_messages`
     - ✅ `pages_messaging`
     - ✅ `pages_manage_metadata`
     - ✅ `pages_read_engagement`

3. Copia el token generado (es temporal, dura 1-2 horas)

### B. Convertir a Token de Larga Duración

⚠️ **IMPORTANTE**: Los tokens temporales expiran rápido. Conviértelo a uno de larga duración:

```bash
curl -i -X GET "https://graph.facebook.com/v18.0/oauth/access_token?grant_type=fb_exchange_token&client_id={TU_APP_ID}&client_secret={TU_APP_SECRET}&fb_exchange_token={TU_TOKEN_TEMPORAL}"
```

Reemplaza:
- `{TU_APP_ID}`: Encuentra en **Settings → Basic → App ID**
- `{TU_APP_SECRET}`: Encuentra en **Settings → Basic → App Secret** (haz clic en "Show")
- `{TU_TOKEN_TEMPORAL}`: El token que copiaste del Graph API Explorer

**Respuesta esperada:**

```json
{
  "access_token": "EAAxxxxxxxxxxxxx...",
  "token_type": "bearer"
}
```

Guarda este `access_token` - **¡Este es tu token de larga duración!**

### C. Obtener el Instagram Business Account ID

1. Primero, obtén el ID de tu página de Facebook:

```bash
curl -i -X GET "https://graph.facebook.com/v18.0/me/accounts?access_token={TU_ACCESS_TOKEN}"
```

**Respuesta:**

```json
{
  "data": [
    {
      "access_token": "...",
      "category": "...",
      "name": "Mi Página",
      "id": "123456789012345",  ← Este es tu PAGE_ID
      ...
    }
  ]
}
```

2. Ahora obtén el Instagram Business Account ID:

```bash
curl -i -X GET "https://graph.facebook.com/v18.0/{PAGE_ID}?fields=instagram_business_account&access_token={TU_ACCESS_TOKEN}"
```

**Respuesta:**

```json
{
  "instagram_business_account": {
    "id": "17841405793187218"  ← Este es tu INSTAGRAM_PAGE_ID
  },
  "id": "123456789012345"
}
```

### D. Crear un Verify Token

Este es un string secreto que tú defines. Puede ser cualquier cosa, por ejemplo:

```bash
# Genera un UUID (Linux/Mac)
uuidgen

# O usa un string personalizado
echo "mi_token_super_secreto_12345"
```

Guarda este valor - lo usarás para configurar el webhook.

---

## 4️⃣ Configurar Webhooks

### Paso 1: Exponer tu Servidor Local con ngrok

En desarrollo, Instagram necesita una URL HTTPS para webhooks. Usa ngrok:

```bash
# Instalar ngrok (si no lo tienes)
# Mac: brew install ngrok
# O descarga desde https://ngrok.com/download

# Exponer el puerto 8080
ngrok http 8080
```

Obtendrás algo como:

```
Forwarding  https://abc123.ngrok.io -> http://localhost:8080
```

**Copia la URL HTTPS** (ej: `https://abc123.ngrok.io`)

### Paso 2: Configurar Webhook en Meta

1. Ve a tu app en Meta for Developers
2. Navega a **"Products"** → **"Messenger"** → **"Settings"** (o Instagram → Settings)
3. En la sección **"Webhooks"**, haz clic en **"Add Callback URL"**

4. Completa:
   - **Callback URL**: `https://abc123.ngrok.io/webhook`
   - **Verify Token**: El token que creaste en el paso 3D

5. Haz clic en **"Verify and Save"**

   ⚠️ Si falla la verificación:
   - Asegúrate que tu app Spring Boot esté corriendo (`mvn spring-boot:run`)
   - Verifica que el verify token en `.env` coincida
   - Revisa los logs de tu aplicación

### Paso 3: Suscribirse a Eventos

1. Después de verificar, verás la lista de **"Webhook Fields"**
2. Suscríbete a:
   - ✅ `messages` (obligatorio)
   - ✅ `messaging_postbacks` (opcional)
   - ✅ `message_echoes` (opcional)

3. Guarda los cambios

### Paso 4: Vincular Página al Webhook

1. En la misma sección de Webhooks, busca **"Subscribe to Pages"**
2. Selecciona tu **página de Facebook**
3. Confirma

---

## 5️⃣ Configuración Local

### Paso 1: Configurar Variables de Entorno

Crea un archivo `.env` en la raíz del proyecto:

```bash
cd /Users/iroldan/Desktop/UADE/Seminario
cp .env.example .env
```

Edita `.env` con tus credenciales:

```env
INSTAGRAM_ACCESS_TOKEN=EAAxxxxxxxxxxxxxxxxxxxxxxxxx
INSTAGRAM_VERIFY_TOKEN=mi_token_super_secreto_12345
INSTAGRAM_PAGE_ID=17841405793187218
SERVER_PORT=8080
```

### Paso 2: Cargar las Variables

```bash
# Linux/Mac
export $(cat .env | xargs)

# O manualmente
export INSTAGRAM_ACCESS_TOKEN="EAAxxxxxxxxxx"
export INSTAGRAM_VERIFY_TOKEN="mi_token_super_secreto"
export INSTAGRAM_PAGE_ID="17841405793187218"
```

### Paso 3: Compilar y Ejecutar

```bash
# Compilar
mvn clean install

# Ejecutar
mvn spring-boot:run
```

Deberías ver:

```
Started InstagramMessagingApplication in X seconds
```

---

## 6️⃣ Probar la Integración

### Test 1: Health Check

```bash
curl http://localhost:8080/webhook/health
```

**Respuesta esperada:** `Webhook activo y funcionando`

### Test 2: Verificación de Webhook (Manual)

```bash
curl "http://localhost:8080/webhook?hub.mode=subscribe&hub.verify_token=mi_token_super_secreto&hub.challenge=test123"
```

**Respuesta esperada:** `test123`

### Test 3: Enviar un Mensaje de Prueba

⚠️ **Nota**: Para enviar mensajes, el usuario debe haberte enviado un mensaje primero (requisito de Instagram).

```bash
curl -X POST http://localhost:8080/api/messages/send \
  -H "Content-Type: application/json" \
  -d '{
    "recipientId": "INSTAGRAM_USER_ID",
    "text": "¡Hola! Este es un mensaje de prueba."
  }'
```

Para obtener un `recipientId` válido:
1. Envíate un mensaje a tu cuenta de Instagram Business desde otra cuenta
2. Revisa los logs de tu aplicación - verás el `senderId`
3. Usa ese ID como `recipientId`

### Test 4: Recibir un Mensaje Real

1. Desde otra cuenta de Instagram, envía un mensaje a tu cuenta de Instagram Business
2. Revisa los logs de tu aplicación Spring Boot
3. Deberías ver:

```
═══════════════════════════════════════
📩 NUEVO MENSAJE DE INSTAGRAM
ID: m_xxx
De: 123456789
Para: 987654321
Tipo: TEXT
Texto: Hola!
Timestamp: 1694564800000
═══════════════════════════════════════
```

---

## 🎉 ¡Listo!

Tu integración con Instagram está funcionando. Ahora puedes:

- ✅ Recibir mensajes de Instagram vía webhooks
- ✅ Enviar mensajes a usuarios de Instagram
- ✅ Procesar y normalizar mensajes

---

## 🐛 Solución de Problemas

### Problema: "Webhook verification failed"

**Solución:**
- Verifica que el `INSTAGRAM_VERIFY_TOKEN` en `.env` coincida con el configurado en Meta
- Asegúrate que la app esté corriendo
- Usa ngrok y verifica que la URL sea correcta
- Revisa los logs: `mvn spring-boot:run`

### Problema: "Invalid access token"

**Solución:**
- Verifica que el token sea de larga duración
- Regenera el token siguiendo el paso 3B
- Asegúrate de tener todos los permisos necesarios

### Problema: "No se reciben mensajes"

**Solución:**
- Verifica que estés suscrito a `messages` en los Webhook Fields
- Verifica que la página esté vinculada al webhook
- Revisa los logs de tu aplicación
- Revisa el **Webhooks Log** en Meta Developer Console

### Problema: "Cannot send message"

**Solución:**
- El usuario debe haberte enviado un mensaje primero (regla de Instagram)
- Verifica que el `recipientId` sea correcto
- Verifica que tengas permisos `instagram_manage_messages`
- Revisa rate limits (100 mensajes/hora por usuario)

---

## 📚 Recursos Adicionales

- [Instagram Messaging API Docs](https://developers.facebook.com/docs/messenger-platform/instagram)
- [Graph API Explorer](https://developers.facebook.com/tools/explorer/)
- [Webhook Testing Tool](https://developers.facebook.com/tools/webhooks/)
- [ngrok Documentation](https://ngrok.com/docs)

---

**¿Problemas?** Revisa los logs con `mvn spring-boot:run` o consulta la documentación oficial de Meta.


