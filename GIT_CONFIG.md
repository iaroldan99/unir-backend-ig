# 🔧 Configuración de Git para este Repositorio

Este repositorio está configurado para usar tu cuenta personal de GitHub en lugar de la cuenta laboral.

## 📋 Configuración Actual

### Este Repositorio (Local)
- **Usuario**: `iaroldan99`
- **Email**: `iaroldan@uade.edu.ar`
- **GitHub**: [@iaroldan99](https://github.com/iaroldan99)

### Configuración Global (Otros Proyectos)
- **Usuario**: `iroldan`
- **Email**: `iaragrisel.roldan@mercadolibre.com`

## 🔍 Verificar Configuración

Para verificar la configuración de este repositorio:

```bash
git config --local user.name
git config --local user.email
```

## 🔄 Cambiar Configuración

Si necesitas cambiar la configuración en el futuro:

```bash
# Cambiar usuario
git config --local user.name "nuevo_usuario"

# Cambiar email
git config --local user.email "nuevo@email.com"
```

## ℹ️ Notas

- La configuración `--local` solo afecta a este repositorio
- Otros repositorios seguirán usando la configuración global (cuenta laboral)
- Los commits realizados antes de este cambio mantendrán el autor anterior
- Para verificar el autor de un commit: `git log --pretty=format:"%an <%ae>"`

## 🚀 Usar con Commits

Cuando hagas commits, automáticamente usará esta configuración:

```bash
git add .
git commit -m "feat: agregar integración con Instagram"
git push origin main
```

Los commits aparecerán en GitHub como realizados por **@iaroldan99**.

---

**Última actualización**: Octubre 2025

