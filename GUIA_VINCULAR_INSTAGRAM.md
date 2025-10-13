# 🚀 Guía Rápida: Vincular Instagram Business y Probar la API

## 📱 Paso 1: Convertir tu Instagram a Cuenta Business

### **Desde tu móvil:**

1. Abre la app de **Instagram**
2. Ve a tu **perfil** (icono de tu foto abajo a la derecha)
3. Toca el **menú** ☰ (arriba a la derecha)
4. Ve a **⚙️ Configuración y privacidad**
5. Toca **Tipo de cuenta y herramientas**
6. Selecciona **Cambiar a cuenta profesional**
7. Elige **Empresa** (no Creator)
8. Completa los detalles:
   - Categoría: elige la que mejor se ajuste
   - Información de contacto: añade email/teléfono

---

## 🔗 Paso 2: Vincular con tu Página de Facebook

### **Opción A: Desde Instagram (Recomendado)**

1. En Instagram: **Configuración** → **Cuenta**
2. Busca **Vinculación de cuentas** o **Cuenta del centro**
3. Toca **Agregar página de Facebook**
4. Inicia sesión en Facebook si es necesario
5. Selecciona una de tus páginas de Facebook existentes
6. Confirma la vinculación

### **Opción B: Desde Facebook**

1. Abre Facebook en el navegador
2. Ve a tu **Página de Facebook**
3. **Configuración** → **Instagram**
4. Haz clic en **Conectar cuenta**
5. Inicia sesión en Instagram
6. Autoriza la conexión

---

## ✅ Paso 3: Verificar la Vinculación

### **Opción 1: Automática (usando el script)**

```bash
cd /Users/iroldan/Desktop/UADE/Seminario
./verificar-instagram.sh
```

**Si todo está bien, verás:**
```
✓ Tu Página
  Page ID: 123456789
  Instagram ID: 17841405793187218  ← Este es tu INSTAGRAM_PAGE_ID
  Instagram Username: @tu_usuario
```

### **Opción 2: Manual (usando curl)**

```bash
# Token que ya tienes configurado
ACCESS_TOKEN="EAALj0YtG29cBPojd5RdcwkZCBsI6j5GeYvamRifNaZBum8GCY1xMgh5krl2E9jRxlDuZBKdSXuwKCtOnUwuCLYF796RLnXIUyGoZAUATOp0oHFOUgvAH5HKbQ2vuGhDPSdRGR5jDxjZAHRZCZA4BZBSXwTI6qcHj7NPAZCkY74hjLbWTv6Ub5fedHZAZCaLkLnnMskVUdscj1kWHWNYjBMdrpawoHbfQ9XkxSow51uMOlKC1JaWiGmOQn2stgZDZD"

# Ver todas tus páginas
curl "https://graph.facebook.com/v18.0/me/accounts?access_token=${ACCESS_TOKEN}" | python3 -m json.tool

# Verificar Instagram de una página específica (reemplaza PAGE_ID)
curl "https://graph.facebook.com/v18.0/PAGE_ID?fields=instagram_business_account&access_token=${ACCESS_TOKEN}" | python3 -m json.tool
```

**Respuesta esperada:**
```json
{
    "instagram_business_account": {
        "id": "17841405793187218"  ← Copia este número
    },
    "id": "123456789"
}
```

---

## 📝 Paso 4: Actualizar el archivo .env

Una vez que tengas el **Instagram Page ID**:

### **Opción A: Editar manualmente**

```bash
nano .env
```

Cambia esta línea:
```
INSTAGRAM_PAGE_ID=your-instagram-business-account-id-here
```

Por:
```
INSTAGRAM_PAGE_ID=17841405793187218  # Usa el ID que obtuviste
```

### **Opción B: Usar comando sed**

```bash
# Reemplaza 17841405793187218 con tu ID real
sed -i '' 's/INSTAGRAM_PAGE_ID=.*/INSTAGRAM_PAGE_ID=17841405793187218/' .env
```

### **Verificar que quedó bien:**

```bash
cat .env | grep INSTAGRAM_PAGE_ID
```

---

## 🔄 Paso 5: Reiniciar la Aplicación

### **1. Cargar las nuevas variables de entorno:**

```bash
export $(cat .env | xargs)
```

### **2. Verificar que se cargaron:**

```bash
echo $INSTAGRAM_PAGE_ID
# Debería mostrar: 17841405793187218 (o el ID que configuraste)
```

### **3. Detener la aplicación actual:**

- Ve a la terminal donde está corriendo Spring Boot
- Presiona **Ctrl + C**

### **4. Reiniciar con las nuevas credenciales:**

```bash
mvn spring-boot:run
```

---

## 🧪 Paso 6: Probar que Funciona

### **Test 1: Health Check**

```bash
curl http://localhost:8080/api/messages/health
```

**Respuesta esperada:**
```json
{"status":"UP","service":"Message Controller"}
```

---

### **Test 2: Enviar Mensaje de Prueba**

⚠️ **IMPORTANTE**: 
- Necesitas que alguien te envíe un mensaje PRIMERO desde Instagram
- Esa es una regla de Instagram: no puedes iniciar conversaciones
- Una vez que te escriben, puedes responder

#### **A. Haz que alguien te escriba:**

1. Desde otra cuenta de Instagram (tuya o de un amigo)
2. Envía un mensaje directo a tu cuenta Business
3. Revisa los **logs de tu aplicación** (donde está corriendo Spring Boot)
4. Verás algo como:

```
📩 NUEVO MENSAJE DE INSTAGRAM
ID: msg_12345
De: 987654321  ← Este es el SENDER_ID que necesitas
```

#### **B. Responde usando la API:**

```bash
# Usa el sender_id que viste en los logs
curl -X POST http://localhost:8080/api/messages/send \
  -H "Content-Type: application/json" \
  -d '{
    "recipientId": "987654321",
    "text": "¡Hola! Este mensaje fue enviado desde mi API 🚀"
  }'
```

**Respuesta esperada (éxito):**
```json
{
  "status": "success",
  "message": "Mensaje enviado correctamente",
  "response": "..."
}
```

---

### **Test 3: Simular Recepción de Mensaje**

Puedes simular que recibes un mensaje (sin necesidad de Instagram real):

```bash
curl -X POST http://localhost:8080/webhook/instagram \
  -H "Content-Type: application/json" \
  -d '{
    "object": "instagram",
    "entry": [{
      "id": "test-123",
      "time": 1696531200,
      "messaging": [{
        "sender": {"id": "user_test_123"},
        "recipient": {"id": "page_test_456"},
        "timestamp": 1696531200000,
        "message": {
          "mid": "msg_test_001",
          "text": "Hola! Este es un mensaje simulado"
        }
      }]
    }]
  }'
```

**Revisa los logs** de tu aplicación y verás:
```
📩 NUEVO MENSAJE DE INSTAGRAM
ID: msg_test_001
De: user_test_123
Texto: Hola! Este es un mensaje simulado
```

---

## 🔍 Solución de Problemas

### **Error: "Invalid OAuth access token"**

❌ **Causa**: El token expiró o no tiene permisos

✅ **Solución**:
1. Ve a: https://developers.facebook.com/tools/explorer/
2. Genera un nuevo token con permisos de PÁGINA
3. Actualiza el `.env`
4. Reinicia la app

---

### **Error: "Instagram account not found"**

❌ **Causa**: El INSTAGRAM_PAGE_ID está mal o no hay cuenta vinculada

✅ **Solución**:
1. Ejecuta: `./verificar-instagram.sh`
2. Verifica que la vinculación esté correcta
3. Actualiza el ID correcto en `.env`

---

### **Error: "Cannot send message to this user"**

❌ **Causa**: El usuario no te ha enviado un mensaje primero

✅ **Solución**:
- Instagram NO permite iniciar conversaciones
- Primero debe escribirte el usuario
- Luego puedes responder durante 24 horas

---

## 📚 Resumen de Comandos

```bash
# 1. Verificar vinculación
./verificar-instagram.sh

# 2. Actualizar .env con el Instagram Page ID
nano .env

# 3. Cargar variables
export $(cat .env | xargs)

# 4. Reiniciar app
mvn spring-boot:run

# 5. Probar health
curl http://localhost:8080/api/messages/health

# 6. Enviar mensaje (después de que te escriban)
curl -X POST http://localhost:8080/api/messages/send \
  -H "Content-Type: application/json" \
  -d '{"recipientId":"SENDER_ID","text":"Hola!"}'
```

---

## 🎯 Checklist Final

- [ ] Instagram convertido a Business
- [ ] Vinculado con página de Facebook
- [ ] Instagram Page ID obtenido
- [ ] Archivo `.env` actualizado
- [ ] Variables de entorno cargadas
- [ ] Aplicación reiniciada
- [ ] Health check funcionando
- [ ] Alguien te envió un mensaje de prueba
- [ ] Respuesta enviada exitosamente

---

## 📞 Enlaces Útiles

- **Tu App**: https://developers.facebook.com/apps/813439077768151/
- **Graph API Explorer**: https://developers.facebook.com/tools/explorer/
- **Convertir a Business**: https://help.instagram.com/502981923235522
- **Vincular cuentas**: https://help.instagram.com/176235449218188

---

**¡Listo! Una vez completado todo esto, tu API estará 100% funcional** 🎉

