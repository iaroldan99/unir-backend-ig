# 📱 Vincular Instagram Business a tu Página de Facebook

## ✅ Lo que ya tienes configurado:

- ✅ Token de acceso de página de Facebook
- ✅ Verify Token para webhooks
- ✅ Aplicación corriendo en `http://localhost:8080`
- ✅ 11 páginas de Facebook disponibles

## ⚠️ Lo que falta:

- ❌ Cuenta de Instagram Business vinculada a una página de Facebook
- ❌ Instagram Page ID

---

## 🎯 Pasos para Vincular Instagram

### **Opción 1: Vincular Instagram Existente (Recomendado)**

Si ya tienes una cuenta de Instagram que quieres usar:

#### **Paso 1: Convertir a Instagram Business**

Desde tu móvil:

1. Abre la app de Instagram
2. Ve a tu perfil → **☰** (menú) → **⚙️ Configuración**
3. Selecciona **Cuenta**
4. Toca **Cambiar tipo de cuenta**
5. Selecciona **Cambiar a cuenta profesional**
6. Elige **Empresa**
7. Completa la información requerida

#### **Paso 2: Vincular con Página de Facebook**

1. En Instagram: **Configuración** → **Cuenta** → **Cuenta del centro**
2. Selecciona **Añadir página de Facebook**
3. Elige una de tus páginas de Facebook
4. Confirma la vinculación

#### **Paso 3: Habilitar Mensajería**

1. Ve a **Configuración** → **Privacidad** → **Mensajes**
2. Asegúrate que la mensajería esté habilitada
3. Permite mensajes de **Todas las personas**

---

### **Opción 2: Crear Nueva Cuenta Business**

Si quieres crear una cuenta de Instagram Business desde cero:

1. Crea una nueva cuenta de Instagram en el móvil
2. Sigue los pasos de "Convertir a Instagram Business" arriba
3. Vincula con una de tus páginas de Facebook

---

## 🔍 Verificar la Vinculación

Una vez que hayas vinculado Instagram, ejecuta este comando para verificar:

```bash
# Reemplaza PAGE_ID con el ID de la página que vinculaste
curl "https://graph.facebook.com/v18.0/PAGE_ID?fields=instagram_business_account&access_token=EAALj0YtG29cBPojd5RdcwkZCBsI6j5GeYvamRifNaZBum8GCY1xMgh5krl2E9jRxlDuZBKdSXuwKCtOnUwuCLYF796RLnXIUyGoZAUATOp0oHFOUgvAH5HKbQ2vuGhDPSdRGR5jDxjZAHRZCZA4BZBSXwTI6qcHj7NPAZCkY74hjLbWTv6Ub5fedHZAZCaLkLnnMskVUdscj1kWHWNYjBMdrpawoHbfQ9XkxSow51uMOlKC1JaWiGmOQn2stgZDZD" | python3 -m json.tool
```

**Ejemplo:**
```bash
# Usa el ID de tu página de Facebook
curl "https://graph.facebook.com/v18.0/TU_PAGE_ID?fields=instagram_business_account&access_token=EAALj0YtG29cBPojd5RdcwkZCBsI6j5GeYvamRifNaZBum8GCY1xMgh5krl2E9jRxlDuZBKdSXuwKCtOnUwuCLYF796RLnXIUyGoZAUATOp0oHFOUgvAH5HKbQ2vuGhDPSdRGR5jDxjZAHRZCZA4BZBSXwTI6qcHj7NPAZCkY74hjLbWTv6Ub5fedHZAZCaLkLnnMskVUdscj1kWHWNYjBMdrpawoHbfQ9XkxSow51uMOlKC1JaWiGmOQn2stgZDZD" | python3 -m json.tool
```

**Resultado esperado:**
```json
{
    "instagram_business_account": {
        "id": "17841405793187218"  ← ¡Este es tu Instagram Page ID!
    },
    "id": "489301328118148"
}
```

---

## 📝 Actualizar el Archivo .env

Una vez que tengas el Instagram Page ID, actualiza el archivo `.env`:

```bash
# Editar manualmente el archivo
nano .env

# O usar este comando (reemplaza el ID)
sed -i '' 's/your-instagram-business-account-id-here/17841405793187218/' .env
```

O simplemente edita la línea en el archivo `.env`:
```
INSTAGRAM_PAGE_ID=TU_INSTAGRAM_PAGE_ID_AQUI
```

---

## 🚀 Probar el Envío de Mensajes

Una vez configurado todo:

### **1. Cargar variables de entorno:**
```bash
export $(cat .env | xargs)
```

### **2. Reiniciar la aplicación** (la que ya está corriendo):

Detén la actual (Ctrl+C en esa terminal) y vuelve a ejecutar:
```bash
mvn spring-boot:run
```

### **3. Probar envío:**

⚠️ **IMPORTANTE**: Primero alguien debe enviarte un mensaje desde Instagram

```bash
# Obtener el sender ID del mensaje que recibiste
# Lo verás en los logs cuando alguien te escriba

# Luego enviar respuesta:
curl -X POST http://localhost:8080/api/messages/send \
  -H "Content-Type: application/json" \
  -d '{
    "recipientId": "SENDER_ID_DEL_MENSAJE",
    "text": "¡Hola! Este es un mensaje desde nuestra API 🚀"
  }'
```

---

## 📥 Probar Recepción de Mensajes (Webhook)

Para recibir mensajes en tiempo real:

### **1. Instalar y ejecutar ngrok:**
```bash
# Si no lo tienes instalado:
brew install ngrok

# Exponer puerto 8080:
ngrok http 8080
```

Copia la URL HTTPS (ej: `https://abc123.ngrok-free.app`)

### **2. Configurar Webhook en Meta:**

1. Ve a: https://developers.facebook.com/apps/813439077768151/messenger/settings/
2. En "Webhooks", haz clic en **"Add Callback URL"**
3. Ingresa:
   - **Callback URL**: `https://TU-URL-NGROK.ngrok-free.app/webhook/instagram`
   - **Verify Token**: `instagram_verify_01fc042c51fc71fe716df639c7e48f99`
4. Haz clic en **"Verify and Save"**
5. Suscríbete a eventos:
   - ✅ `messages`
   - ✅ `messaging_postbacks`
   - ✅ `message_echoes`

### **3. Probar:**

1. Desde otra cuenta de Instagram, envía un mensaje a tu cuenta Business
2. Verás el mensaje en los logs de tu aplicación
3. Tu API lo procesará automáticamente

---

## 🔍 Comandos Útiles de Verificación

### **Ver todas tus páginas:**
```bash
curl "https://graph.facebook.com/v18.0/me/accounts?access_token=TU_TOKEN" | python3 -m json.tool
```

### **Verificar Instagram de una página específica:**
```bash
curl "https://graph.facebook.com/v18.0/PAGE_ID?fields=instagram_business_account&access_token=TU_TOKEN" | python3 -m json.tool
```

### **Ver información de tu Instagram Business Account:**
```bash
curl "https://graph.facebook.com/v18.0/INSTAGRAM_PAGE_ID?fields=id,username,name,profile_picture_url&access_token=TU_TOKEN" | python3 -m json.tool
```

---

## 🎯 Resumen Rápido

```bash
# 1. Vincular Instagram Business en el móvil
# 2. Verificar vinculación:
curl "https://graph.facebook.com/v18.0/489301328118148?fields=instagram_business_account&access_token=TOKEN"

# 3. Actualizar .env con el Instagram Page ID
nano .env

# 4. Cargar variables y reiniciar:
export $(cat .env | xargs)
mvn spring-boot:run

# 5. ¡Listo para enviar/recibir mensajes!
```

---

## 📚 Links Útiles

- **Tu App Dashboard**: https://developers.facebook.com/apps/813439077768151/
- **Convertir a Business**: https://help.instagram.com/502981923235522
- **Vincular Instagram y Facebook**: https://help.instagram.com/176235449218188
- **Messenger Settings**: https://developers.facebook.com/apps/813439077768151/messenger/settings/

---

**¡Estás a solo 3 pasos de tener todo funcionando!** 🚀

1. Vincular Instagram Business con una página
2. Obtener el Instagram Page ID
3. Actualizar el archivo `.env`

