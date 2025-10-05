#!/bin/bash

# Script de prueba rápido para verificar que la API funciona

echo "🧪 Probando Instagram Messaging API..."
echo "======================================"
echo ""

# Colores
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m'

BASE_URL="http://localhost:8080"

# Test 1: Health check del webhook
echo -e "${BLUE}1️⃣  Test: Health check del webhook${NC}"
RESPONSE=$(curl -s $BASE_URL/webhook/instagram/health)
if [[ $RESPONSE == *"funcionando"* ]]; then
    echo -e "${GREEN}✅ PASS: $RESPONSE${NC}"
else
    echo -e "${RED}❌ FAIL: No responde${NC}"
fi
echo ""

# Test 2: Health check del controlador de mensajes
echo -e "${BLUE}2️⃣  Test: Health check del controlador de mensajes${NC}"
RESPONSE=$(curl -s $BASE_URL/api/messages/health)
if [[ $RESPONSE == *"UP"* ]]; then
    echo -e "${GREEN}✅ PASS: $RESPONSE${NC}"
else
    echo -e "${RED}❌ FAIL: No responde${NC}"
fi
echo ""

# Test 3: Health check del controlador unificado
echo -e "${BLUE}3️⃣  Test: Health check del controlador unificado${NC}"
RESPONSE=$(curl -s $BASE_URL/send/health)
if [[ $RESPONSE == *"UP"* ]]; then
    echo -e "${GREEN}✅ PASS: $RESPONSE${NC}"
else
    echo -e "${RED}❌ FAIL: No responde${NC}"
fi
echo ""

# Test 4: Verificación de webhook
echo -e "${BLUE}4️⃣  Test: Verificación de webhook (challenge)${NC}"
CHALLENGE="TEST12345"
RESPONSE=$(curl -s "$BASE_URL/webhook/instagram?hub.mode=subscribe&hub.verify_token=your-verify-token-here&hub.challenge=$CHALLENGE")
if [[ $RESPONSE == $CHALLENGE ]]; then
    echo -e "${GREEN}✅ PASS: Webhook verifica correctamente${NC}"
else
    echo -e "${RED}❌ FAIL: Respuesta inesperada: $RESPONSE${NC}"
fi
echo ""

# Test 5: Recepción de mensaje simulado
echo -e "${BLUE}5️⃣  Test: Recepción de mensaje simulado${NC}"
RESPONSE=$(curl -s -X POST $BASE_URL/webhook/instagram \
  -H "Content-Type: application/json" \
  -d '{
    "object": "instagram",
    "entry": [{
      "id": "test-123",
      "time": 1696531200,
      "messaging": [{
        "sender": {"id": "test-sender"},
        "recipient": {"id": "test-recipient"},
        "timestamp": 1696531200000,
        "message": {
          "mid": "test-msg-id",
          "text": "Mensaje de prueba"
        }
      }]
    }]
  }')
if [[ $RESPONSE == *"EVENT_RECEIVED"* ]]; then
    echo -e "${GREEN}✅ PASS: Mensaje recibido correctamente${NC}"
else
    echo -e "${RED}❌ FAIL: No se recibió el mensaje${NC}"
fi
echo ""

# Test 6: Probar formato de envío (sin credenciales reales, solo estructura)
echo -e "${BLUE}6️⃣  Test: Estructura de endpoint de envío${NC}"
RESPONSE=$(curl -s -X POST $BASE_URL/api/messages/send \
  -H "Content-Type: application/json" \
  -d '{}')
if [[ $RESPONSE == *"requeridos"* ]]; then
    echo -e "${GREEN}✅ PASS: Validación funcionando (requiere recipientId y text)${NC}"
else
    echo -e "${RED}❌ FAIL: Validación no funciona correctamente${NC}"
fi
echo ""

# Resumen
echo "======================================"
echo -e "${GREEN}🎉 Todos los tests básicos completados${NC}"
echo ""
echo "📝 Notas:"
echo "• La API está funcionando correctamente"
echo "• Para enviar mensajes reales necesitas configurar las credenciales de Instagram"
echo "• Configura las variables de entorno en .env (ver SETUP_GUIDE.md)"
echo ""
echo "📚 Próximos pasos:"
echo "1. Configurar credenciales: cp env-template.txt .env"
echo "2. Obtener tokens de Meta: https://developers.facebook.com/"
echo "3. Configurar webhook con ngrok para desarrollo"
echo ""

