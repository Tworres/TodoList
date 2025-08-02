# Todo List API

Uma API RESTful para gerenciamento de tarefas (todo) que expõe endpoints para serem consumidos por aplicações externas.

## Tecnologias Utilizadas

- Java 24
- Spring Boot 3.5.4
- Spring Data JPA
- H2 Database (banco de dados em memória)
- Lombok
- JUnit 5 para testes

## Configuração e Execução

1. Clone o repositório
2. Execute o projeto usando Maven:
   ```
   ./mvnw spring-boot:run
   ```
3. A aplicação estará disponível em `http://localhost:8080`
4. O console do H2 estará disponível em `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:tododb`
   - Username: `sa`
   - Password: `password`

## Endpoints da API

### Criar uma nova tarefa

```
POST /api/todos
```

**Corpo da requisição:**
```json
{
  "title": "Título da tarefa",
  "description": "Descrição da tarefa"
}
```

**Resposta:**
```json
{
  "id": 1,
  "title": "Título da tarefa",
  "description": "Descrição da tarefa",
  "completed": false,
  "createdAt": "2025-08-02T13:58:00",
  "updatedAt": null,
  "completedAt": null
}
```

### Listar todas as tarefas

```
GET /api/todos
```

**Resposta:**
```json
[
  {
    "id": 1,
    "title": "Título da tarefa 1",
    "description": "Descrição da tarefa 1",
    "completed": false,
    "createdAt": "2025-08-02T13:58:00",
    "updatedAt": null,
    "completedAt": null
  },
  {
    "id": 2,
    "title": "Título da tarefa 2",
    "description": "Descrição da tarefa 2",
    "completed": true,
    "createdAt": "2025-08-02T13:58:00",
    "updatedAt": "2025-08-02T14:00:00",
    "completedAt": "2025-08-02T14:00:00"
  }
]
```

### Filtrar tarefas

```
GET /api/todos?completed=true
GET /api/todos?title=Título
GET /api/todos?description=Descrição
```

### Obter uma tarefa específica

```
GET /api/todos/{id}
```

**Resposta:**
```json
{
  "id": 1,
  "title": "Título da tarefa",
  "description": "Descrição da tarefa",
  "completed": false,
  "createdAt": "2025-08-02T13:58:00",
  "updatedAt": null,
  "completedAt": null
}
```

### Atualizar uma tarefa

```
PUT /api/todos/{id}
```

**Corpo da requisição:**
```json
{
  "title": "Título atualizado",
  "description": "Descrição atualizada",
  "completed": true
}
```

**Resposta:**
```json
{
  "id": 1,
  "title": "Título atualizado",
  "description": "Descrição atualizada",
  "completed": true,
  "createdAt": "2025-08-02T13:58:00",
  "updatedAt": "2025-08-02T14:05:00",
  "completedAt": "2025-08-02T14:05:00"
}
```

### Marcar uma tarefa como concluída

```
PATCH /api/todos/{id}/complete
```

**Resposta:**
```json
{
  "id": 1,
  "title": "Título da tarefa",
  "description": "Descrição da tarefa",
  "completed": true,
  "createdAt": "2025-08-02T13:58:00",
  "updatedAt": "2025-08-02T14:10:00",
  "completedAt": "2025-08-02T14:10:00"
}
```

### Excluir uma tarefa

```
DELETE /api/todos/{id}
```

**Resposta:**
```json
{
  "deleted": true
}
```

## Estrutura do Projeto

- `model`: Contém a entidade Todo
- `repository`: Contém a interface TodoRepository para acesso ao banco de dados
- `service`: Contém a interface TodoService e sua implementação
- `controller`: Contém o TodoController com os endpoints REST

## Testes

Os testes de integração estão disponíveis em `src/test/java/com/tworres/todolist/controller/TodoControllerIntegrationTest.java`

Para executar os testes:
```
./mvnw test
```