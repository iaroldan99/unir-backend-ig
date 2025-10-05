# 🔥 Comandos cURL - Instagram Messaging API

Todos los comandos listos para copiar y pegar.

---

## ✅ 1. Health Checks

### Webhook Health
```bash
curl http://localhost:8080/webhook/instagram/health
```

### Messages Controller Health
```bash
curl http://localhost:8080/api/messages/health
```

### Unified Controller Health
```bash
curl http://localhost:8080/send/health
```

### Todos los Health Checks (con formato)
```bash
echo "=== Webhook Health ===" && \
curl -s http://localhost:8080/webhook/instagram/health && \
echo -e "\n\n=== Messages Health ===" && \
curl -s http://localhost:8080/api/messages/health | jq . && \
echo -e "\n=== Unified Health ===" && \
curl -s http://localhost:8080/send/health | jq .
```

---

## 🔐 2. Verificar Webhook

### Challenge de Meta
```bash
curl "http://localhost:8080/webhook/instagram?hub.mode=subscribe&hub.verify_token=your-verify-token-here&hub.challenge=TEST12345"
```

**Respuesta esperada:** `TEST12345`

---

## 📥 3. Recibir Mensajes (Simular Webhook)

### Mensaje de Texto Simple
```bash
curl -X POST http://localhost:8080/webhook/instagram \
  -H "Content-Type: application/json" \
  -d '{
    "object": "instagram",
    "entry": [{
      "id": "test-page-123",
      "time": 1696531200,
      "messaging": [{
        "sender": {"id": "user-456"},
        "recipient": {"id": "bot-789"},
        "timestamp": 1696531200000,
        "message": {
          "mid": "msg-001",
          "text": "Hola desde cURL! 👋"
        }
      }]
    }]
  }'
```

### Mensaje con Emoji
```bash
curl -X POST http://localhost:8080/webhook/instagram \
  -H "Content-Type: application/json" \
  -d '{
    "object": "instagram",
    "entry": [{
      "id": "test-123",
      "time": 1696531200,
      "messaging": [{
        "sender": {"id": "user-emoji"},
        "recipient": {"id": "bot-123"},
        "timestamp": 1696531200000,
        "message": {
          "mid": "msg-emoji",
          "text": "¡Hola! 😊 ¿Cómo estás? 🚀"
        }
      }]
    }]
  }'
```

### Mensaje con Imagen
```bash
curl -X POST http://localhost:8080/webhook/instagram \
  -H "Content-Type: application/json" \
  -d '{
    "object": "instagram",
    "entry": [{
      "id": "test-img",
      "time": 1696531200,
      "messaging": [{
        "sender": {"id": "user-img"},
        "recipient": {"id": "bot-img"},
        "timestamp": 1696531200000,
        "message": {
          "mid": "msg-img-001",
          "text": "Mira esta imagen",
          "attachments": [{
            "type": "image",
            "payload": {
              "url": "https://picsum.photos/200/300"
            }
          }]
        }
      }]
    }]
  }'
```

### Mensaje con Video
```bash
curl -X POST http://localhost:8080/webhook/instagram \
  -H "Content-Type: application/json" \
  -d '{
    "object": "instagram",
    "entry": [{
      "id": "test-video",
      "time": 1696531200,
      "messaging": [{
        "sender": {"id": "user-video"},
        "recipient": {"id": "bot-video"},
        "timestamp": 1696531200000,
        "message": {
          "mid": "msg-video-001",
          "text": "Te envío un video",
          "attachments": [{
            "type": "video",
            "payload": {
              "url": "https://example.com/video.mp4"
            }
          }]
        }
      }]
    }]
  }'
```

**Respuesta esperada:** `EVENT_RECEIVED`

---

## 📤 4. Enviar Mensajes

⚠️ **Nota:** Para enviar mensajes reales necesitas:
1. Configurar credenciales en `.env`
2. El usuario debe haberte enviado un mensaje primero
3. Reemplazar `INSTAGRAM_USER_ID` con un ID real

### Formato Unificado (Recomendado)
```bash
curl -X POST http://localhost:8080/send/instagram \
  -H "Content-Type: application/json" \
  -d '{
    "recipient": "INSTAGRAM_USER_ID",
    "message": "¡Hola desde la API! 🚀"
  }'
```

### Formato Alternativo
```bash
curl -X POST http://localhost:8080/api/messages/send \
  -H "Content-Type: application/json" \
  -d '{
    "recipientId": "INSTAGRAM_USER_ID",
    "text": "¡Hola desde cURL! 🎉"
  }'
```

### Por User ID (Path Variable)
```bash
curl -X POST http://localhost:8080/send/instagram/INSTAGRAM_USER_ID/messages \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Mensaje directo ✨"
  }'
```

### Con Formato de Respuesta
```bash
curl -X POST http://localhost:8080/send/instagram \
  -H "Content-Type: application/json" \
  -d '{
    "recipient": "INSTAGRAM_USER_ID",
    "message": "Test desde cURL"
  }' | jq .
```

---

## 🧪 5. Tests de Validación

### Test: Sin Recipient (debe fallar)
```bash
curl -X POST http://localhost:8080/api/messages/send \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Mensaje sin destinatario"
  }' | jq .
```

**Respuesta esperada:**
```json
{
  "error": "recipientId y text son requeridos"
}
```

### Test: Sin Texto (debe fallar)
```bash
curl -X POST http://localhost:8080/api/messages/send \
  -H "Content-Type: application/json" \
  -d '{
    "recipientId": "123456789"
  }' | jq .
```

**Respuesta esperada:**
```json
{
  "error": "recipientId y text son requeridos"
}
```

### Test: Ambos Formatos (debe funcionar)
```bash
# Formato 1: recipient/message
curl -X POST http://localhost:8080/send/instagram \
  -H "Content-Type: application/json" \
  -d '{"recipient": "123", "message": "Test 1"}' | jq .

# Formato 2: recipientId/text
curl -X POST http://localhost:8080/api/messages/send \
  -H "Content-Type: application/json" \
  -d '{"recipientId": "123", "text": "Test 2"}' | jq .
```

---

## 📊 6. Suite Completa de Tests

### Ejecutar Todos los Tests Básicos
```bash
#!/bin/bash
echo "🧪 Ejecutando suite de tests..."
echo ""

echo "✅ Test 1: Webhook Health"
curl -s http://localhost:8080/webhook/instagram/health
echo -e "\n"

echo "✅ Test 2: Messages Health"
curl -s http://localhost:8080/api/messages/health | jq .
echo ""

echo "✅ Test 3: Unified Health"
curl -s http://localhost:8080/send/health | jq .
echo ""

echo "✅ Test 4: Webhook Challenge"
RESPONSE=$(curl -s "http://localhost:8080/webhook/instagram?hub.mode=subscribe&hub.verify_token=your-verify-token-here&hub.challenge=TEST12345")
if [ "$RESPONSE" = "TEST12345" ]; then
  echo "✅ PASS: $RESPONSE"
else
  echo "❌ FAIL: $RESPONSE"
fi
echo ""

echo "✅ Test 5: Recibir Mensaje"
curl -s -X POST http://localhost:8080/webhook/instagram \
  -H "Content-Type: application/json" \
  -d '{
    "object": "instagram",
    "entry": [{
      "id": "test",
      "time": 1696531200,
      "messaging": [{
        "sender": {"id": "test-user"},
        "recipient": {"id": "test-bot"},
        "timestamp": 1696531200000,
        "message": {"mid": "test-msg", "text": "Test"}
      }]
    }]
  }'
echo -e "\n"

echo "✅ Test 6: Validación (sin recipient)"
curl -s -X POST http://localhost:8080/api/messages/send \
  -H "Content-Type: application/json" \
  -d '{"text": "Sin recipient"}' | jq .
echo ""

echo "🎉 Tests completados"
```

---

## 🔍 7. Debug y Troubleshooting

### Ver Headers de Respuesta
```bash
curl -i http://localhost:8080/webhook/instagram/health
```

### Verbose (ver todo el request/response)
```bash
curl -v -X POST http://localhost:8080/webhook/instagram \
  -H "Content-Type: application/json" \
  -d '{"object": "instagram", "entry": []}'
```

### Medir Tiempo de Respuesta
```bash
curl -w "\n\nTiempo: %{time_total}s\n" \
  http://localhost:8080/webhook/instagram/health
```

### Guardar Respuesta en Archivo
```bash
curl -X POST http://localhost:8080/webhook/instagram \
  -H "Content-Type: application/json" \
  -d @mensaje.json \
  -o respuesta.txt
```

---

## 📝 8. Variables de Entorno

### Usar Variables en cURL
```bash
# Definir variables
export API_URL="http://localhost:8080"
export RECIPIENT_ID="123456789"
export MESSAGE="Hola desde variable"

# Usar en curl
curl -X POST $API_URL/send/instagram \
  -H "Content-Type: application/json" \
  -d "{\"recipient\": \"$RECIPIENT_ID\", \"message\": \"$MESSAGE\"}"
```

---

## 🚀 9. Producción

### Apuntar a Servidor Remoto
```bash
# Cambiar localhost por tu dominio
curl https://tu-dominio.com/webhook/instagram/health
```

### Con Autenticación (futuro)
```bash
curl -X POST https://tu-dominio.com/send/instagram \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_AQUI" \
  -d '{"recipient": "123", "message": "Hola"}'
```

---

## 💡 Tips

### 1. Usar `jq` para formatear JSON
```bash
# Instalar jq (Mac)
brew install jq

# Usar con curl
curl -s http://localhost:8080/api/messages/health | jq .
```

### 2. Crear Alias
```bash
# Agregar a ~/.bashrc o ~/.zshrc
alias api-health="curl -s http://localhost:8080/send/health | jq ."
alias api-test="curl -s http://localhost:8080/webhook/instagram/health"

# Usar
api-health
api-test
```

### 3. Script de Test Rápido
```bash
# Guardar en test-quick.sh
#!/bin/bash
curl -s http://localhost:8080/webhook/instagram/health && echo " ✅"
```

---

## 📚 Recursos

- **Scripts automatizados**: `./test-api.sh`
- **Ejemplos detallados**: `./curl-examples.sh`
- **Colección Postman**: `Instagram_API.postman_collection.json`
- **Documentación**: `README.md`

---

## 🆘 Problemas Comunes

### Error: Connection refused
```bash
# Solución: Verifica que la API esté corriendo
ps aux | grep java
# O reinicia
mvn spring-boot:run
```

### Error: 404 Not Found
```bash
# Verifica la URL exacta
curl -i http://localhost:8080/webhook/instagram/health
```

### JSON malformado
```bash
# Usa un validador online primero
# https://jsonlint.com/
```

---

**Última actualización**: Octubre 2025  
**Equipo 3 - Instagram Integration** | UADE Seminario

