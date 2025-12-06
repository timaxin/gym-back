# ТЗ: FitnessBooking — система записи на тренировки (MVP → full)

Краткое описание

**FitnessBooking** — SPA на TypeScript + React для записи на тренировки с бэкендом на Java (предпочтительно). MVP: пользователи, один админ (создаёт тренеров, слоты и т.п.), возможность бронировать слоты (индивидуальные/групповые), отменять в рамках правил, хранить историю. Система должна быть промышленно структурирована, с акцентом на корректную типизацию, оптимизацию ререндеров, кеширование, lazy loading и тестирование после MVP.

## Цели и приоритеты проекта

Цель MVP: дать пользователю и тренеру возможность:

- просматривать календарь слотов (30-мин шаги),
- записываться на тренировки (индивидуальные и групповые),
- отменять бронирование в соответствии с правилами,

Админ управляет тренерами/слотами/ролями.

Фокус для ученика (learning outcomes):

- строгая TS-модель (types/interfaces)
- архитектура React + правильный state management (React Query + Context / Zustand)
- оптимизация ререндеров (useMemo, useCallback, React.memo, virtualization)
- взаимодействие с REST API (auth JWT)
- тестирование (unit & integration после MVP)
- логическая модель, обработка ошибок, logging

## Пользователи и роли (MVP → расширение)

**Guest:** просматривает публичные расписания (опционально).

**User (клиент):** регистрируется/логинится, бронирует/отменяет свои бронирования (отмена не позднее чем за 6 часов).

**Coach / Trainer:** может создавать тренировки (индивидуальные / групповые), отменять с указанием причины; если отмена «без причины», действует правило «за 6 часов компенсация» (т.е. считается несанкционированной) — логика согласовать.

**Club Admin:** полный контроль — создаёт/удаляет слоты, управляет ролями тренеров и пользователей; при этом не видит лишней личной информации (по требованию).

**System Admin (MVP: один admin):** создаёт тренеров и управляет всего основного (для MVP — одна учётка).

## Бизнес-правила (важно)

### Слоты:

Шаг 30 минут (время начала: 00 / 30 минут).
Длина тренировки: 30, 60, 90, 120 минут (макс).

Слот может быть индивидуальным (capacity = 1) или групповым (capacity > 1).
Слоты привязаны к тренеру и к локации (опционально).

### Бронирование:

Пользователь может записаться, если слот не заполнен.

Отмена пользователем возможна не позднее, чем за 6 часов до начала; иначе отмена запрещена или помечается (правило: откат/штраф).

Тренер может отменить в любое время; если отмена без причины (определяется административной процедурой), система помечает это в логе (и возможно инициирует компенсацию).

Club Admin может отменять в любое время и обязан указать причину.

### Ограничения тренера:

Отпуск: тренер может помечать дни как недоступные.

Лимит часов работы в день: задаётся (например 8 часов) — система не позволит создавать пересекающиеся слоты, если суммарная длительность > лимит.

### История и логирование:

Вся история бронирований сохраняется.

Все действия (создание/изменение/удаление слотов, бронирования, отмены) записываются в журнал (Logger) с полем reason (если задано), timestamp, userId/trainerId/adminId.

## Технологический стек (рекомендация)

### Frontend:

- React + TypeScript (strict)
- Vite (или Create React App), ESLint, Prettier, Husky (pre-commit) — по желанию
- React Router (SPA), React Query (data fetching + cache), Zustand (или Context) для локального UI state
- React Developer Tools + React Profiler
- CSS: Tailwind CSS или CSS Modules (по вкусу)
- Testing: Jest + React Testing Library (post-MVP unit/integration)
- Optional: react-virtualized / react-window для длинных списков

### Backend:

- Java Spring Boot (REST), PostgreSQL
- Auth: JWT (access + refresh optional)
- Logging: structured logs (e.g., Logback), audit table
- Migrations: Flyway / Liquibase

### Архитектура фронтенда (директория)
```bash
src/
  api/                # wrappers for REST endpoints (axios fetchers)
  components/
    SlotCard/
      SlotCard.tsx
      SlotCard.css
    Calendar/
      Calendar.tsx
    BookingModal/
      BookingModal.tsx
    Header/
    Footer/
    Dashboard/
  hooks/
    useAuth.ts
    useSlots.ts      # react-query hooks
    useDebounce.ts
  pages/
    Home.tsx
    Login.tsx
    DashboardUser.tsx
    DashboardTrainer.tsx
    AdminPanel.tsx
  store/              # zustand or context for UI state
  types/              # TS interfaces
  utils/
    date.ts
    validators.ts
  App.tsx
  main.tsx
```

### Дерево компонентов (вкратце)

```bash
App (routes)
Header
Routes
Home (calendar + search)
Calendar (month / week / day view)
SlotCard (each slot)
BookingModal (form: choose slot, number of participants, confirm)
DashboardUser (my bookings, history)
BookingList
DashboardTrainer (my slots, create slot)
AdminPanel (manage trainers, manage slots)
Toasts
ErrorBoundary
```

### API: основные эндпоинты (REST)

| Базовый путь: `/api/v1`

#### Auth

* POST `/auth/register` — { name, email, password } → 201 + { securityUser, token }
* POST `/auth/login` — { email, password } → 200 + { securityUser, token }
* POST `/auth/refresh` — refresh token

#### Users

* GET `/users/:id` — profile (admin может получить резюме, но не личную инфо у других)
* PUT `/users/:id` — update profile (securityUser or admin)

#### Trainers (admin operations)

* GET `/trainers` — list (with filters)
* POST `/trainers` — create trainer (admin)
* PUT `/trainers/:id` — update
* DELETE `/trainers/:id` — delete

#### Slots
> _supports lazy-loading (pagination) and filters (date range, trainer, type) for GET /slots_
* GET `/slots` — list (query: from, to, trainerId, status, page, limit, search)
* GET `/slots/:id` — details
* POST `/slots` — create slot (trainer or admin) { trainerId, start, duration, capacity, type (group/individual), location, reason }
* PUT `/slots/:id` — update slot
* DELETE `/slots/:id` — delete/cancel slot — must accept { reason, cancelledBy } and create log

#### Bookings
> _supports lazy-loading (pagination) and filters (date range, trainer, type) for GET /bookings_

* GET `/bookings` — list for current securityUser / admin filter
* GET `/bookings/:id`
> _server validates capacity, double booking, securityUser daily limit etc for POST /bookings_
* POST `/bookings` — create booking { slotId, userId, participantsCount }
* PUT `/bookings/:id/cancel` — cancel booking { reason } — enforces 6-hour rule for securityUser cancellations
* DELETE `/bookings/:id` — admin cancellation

#### Admin / Logs

* GET `/logs` — list actions (paginated)
* GET `/stats` — aggregated statistics (optional)

### Response examples (booking)

* POST `/bookings` success: 201 { bookingId, slotId, userId, status: 'confirmed' }
* error: 400 { code: 'SLOT_FULL' }, 403 { code: 'CANCELLATION_TOO_LATE' }

### Типизация (TypeScript interfaces — пример)
```typescript
// src/types/models.ts
export type Role = 'securityUser' | 'trainer' | 'admin' | 'club_admin';

export interface User {
  id: string;
  name: string;
  email: string;
  role: Role;
  // no sensitive fields visible to club admins (per requirement)
}

export interface Trainer {
  id: string;
  name: string;
  bio?: string;
  dailyHourLimit: number; // in minutes or hours
}

export type SlotType = 'individual' | 'group';

export interface Slot {
  id: string;
  trainerId: string;
  start: string; // ISO datetime
  duration: number; // minutes: 30 | 60 | 90 | 120
  capacity: number; // 1 for individual
  type: SlotType;
  location?: string;
  canceled?: boolean;
  cancelReason?: string;
  createdBy: string; // userId or trainerId or adminId
}

export interface Booking {
  id: string;
  slotId: string;
  userId: string;
  createdAt: string;
  status: 'confirmed' | 'cancelled';
  cancelReason?: string;
  participantsCount?: number; // for group bookings securityUser may register multiple seats (optional)
}
```

## Frontend: получение данных и управление состоянием

**Рекомендация**: использовать React Query в качестве основного слоя для работы с данными:
- Кеширование данных с настройкой `staleTime`/`cacheTime`
- Поддержка оптимистичных обновлений (`useMutation` + `onMutate`/`onError`/`onSettled`)
- Автоматический повторный запрос при фокусе окна (можно отключить)

**Управление состоянием UI**:
- Простые состояния (модальные окна, локальные фильтры) — Context или `useState`
- Сложные состояния — Zustand

**Паттерны**:
- `useSlots(from, to, filters, page)` — хук для ленивой загрузки слотов
- `useBookingMutation()` — для бронирования (с оптимистичными обновлениями)
- `useDebounce` — обязательно для поиска и фильтров

## Оптимизация ререндеров (обязательные требования)

**Компоненты**:
- `SlotCard` — должен быть мемоизирован (`React.memo`) и принимать только примитивные пропсы
- `Calendar` — использовать виртуализацию для отображения большого количества слотов

**Обработчики событий**:
- Все колбэки, передаваемые в дочерние компоненты, должны быть мемоизированы (`useCallback`)

**Производные данные**:
- `filteredSlots`, `slotsByDay` и т.п. — вычислять через `useMemo`

**Загрузка данных**:
- Кеширование результатов (настройка `staleTime`)
- Использовать пагинацию и ленивую загрузку

**Политика рендеринга**:
- Использовать React DevTools Profiler для выявления и устранения лишних ререндеров

## Валидация и обработка ошибок

**Клиентская валидация**:
- Использовать `zod` или `yup`

**Серверная валидация (обязательная)**:
- `start` должен быть выровнен по границе 30 минут
- `duration` ∈ {30, 60, 90, 120} минут
- `capacity` ≥ 1
- Запрет наложения слотов для тренера сверх дневного лимита
- Количество бронирований не может превышать вместимость
- Соблюдение временных окон отмены

**Формат ошибок API**:
```json
{
  "error": {
    "code": "SLOT_FULL",
    "message": "Нет свободных мест",
    "details": { ... }
  }
}
```

**Обработка ошибок**:
- Frontend: показ уведомлений (toast) + логирование ошибок на стороне клиента
- Backend: логирование в аудит-журнал

## Логирование и аудит

**Бэкенд**:
- Запись аудита для каждой мутации (создание/отмена слота, создание/отмена бронирования)
- Поля записи: `timestamp`, `userId`, `actionType`, `entityId`, `reason`

**Фронтенд**:
- Локальные логи отладки (только для разработки)
- Возможность просмотра аудита (для администраторов)

## Безопасность

- Аутентификация через JWT (короткоживущий access token, опционально refresh token)
- Защита эндпоинтов с помощью RBAC (ролевая модель доступа)
- Сокрытие PII (персональных данных) от администраторов клуба
  - API возвращает ограниченную информацию о пользователях для определенных ролей

## Режим реального времени (post-MVP)

- Предложить использование WebSocket для обновления информации о слотах в реальном времени
- В MVP достаточно использовать polling или refetch в React Query

## Тестирование и качество кода

**MVP**:
- Модульные тесты для утилит и основных хуков
- Интеграционные тесты для ключевых сценариев (бронирование/отмена)

**После MVP**:
- Сквозные (E2E) тесты с использованием Cypress
- Настройка ESLint + Prettier
- Pre-commit хуки с Husky

## Разработка / Деплой / Инфраструктура (кратко)

- **Локальная разработка**: docker-compose (бэкенд + PostgreSQL)
- **CI**: запуск линтеров, проверка типов, модульные тесты
- **Деплой**: контейнеризированные приложения (опционально)
- **Миграции БД**: с использованием Flyway

## Критерии приемки (MVP)

**Функциональные требования**:
- Пользователь может зарегистрироваться/войти через JWT и забронировать доступный слот (групповой/индивидуальный)
- Пользователь может отменить бронирование, если до начала слота осталось > 6 часов
- Тренер может создавать слоты (с валидацией)
- Администратор клуба может создавать/отменять любые слоты
- Слоты отображаются в календаре с визуальными состояниями: свободные / мало мест / заполненные / отмененные
- Бронирования сохраняются и отображаются в истории пользователя
- Показ уведомлений об успехе/ошибке
- Базовое логирование действий

**Нефункциональные требования**:
- Приложение на TypeScript (без `any` для основных моделей)
- Использование React Query для получения данных
- Использование useMemo, useCallback, React.memo где уместно
- Настроенные ESLint + Prettier
- Ленивая загрузка для длинных списков

## Модели данных (ER-диаграмма)

```
[User] 1---* [Booking] *---1 [Slot] *---1 [Trainer]

User(id, name, email, role)
Trainer(id, userId?, name, dailyHourLimit)
Slot(id, trainerId, start, duration, capacity, type, canceled, createdBy)
Booking(id, slotId, userId, participantsCount, status, createdAt, cancelReason)
Log(id, actorId, action, entityType, entityId, reason, timestamp)
```

## Примеры API

**Создание слота**:
```http
POST /api/v1/slots
Authorization: Bearer <token>
Content-Type: application/json

{
  "trainerId": "tr_123",
  "start": "2025-12-01T10:00:00Z",
  "duration": 60,
  "capacity": 10,
  "type": "group",
  "location": "Зал A"
}
```

**Создание бронирования**:
```http
POST /api/v1/bookings
Content-Type: application/json

{
  "slotId": "slot_456",
  "userId": "user_789",
  "participantsCount": 1
}
```

**Отмена бронирования**:
```http
PUT /api/v1/bookings/booking_12/cancel
Content-Type: application/json

{ "reason": "Не смогу прийти" }
```

**Коды ответов**:
- 200 OK
- 400 Bad Request
- 403 Forbidden (слишком поздно для отмены)
- 409 Conflict (слот заполнен)

## Детали интерфейса

**Представление календаря**:
- По умолчанию — неделя
- Возможность раскрытия дня по клику

**Состояния слотов**:
- Свободно (осталось > 2 мест)
- Мало мест (≤ 2 места)
- Нет мест
- Отменен

**Модальное окно бронирования**:
- Детали слота
- Имя тренера
- Оставшееся количество мест
- Кнопка подтверждения

**Дополнительно**:
- Поиск и фильтры (тренер, тип, время)
- Доступность: навигация с клавиатуры по дням/слотам

## Этапы разработки (план спринтов)

**Спринт 0 (настройка) — 1 день**
- Настройка репозитория, tsconfig, eslint/prettier
- CI/CD скелет
- Заглушки эндпоинтов бэкенда
- Миграции БД

**Спринт 1 (MVP бэкенд и минимальный фронтенд) — 3–5 дней**
- Аутентификация (JWT)
- Модели: User, Trainer, Slot, Booking, Log
- Эндпоинты: список слотов, создание/отмена бронирования
- Фронтенд: вход, календарь, список слотов, модальное окно бронирования, уведомления, локальная аутентификация

**Спринт 2 (бизнес-правила + админка + оптимизации) — 3–5 дней**
- Панели управления для тренеров и администраторов
- Применение бизнес-правил (отмена за 6 часов)
- Настройка кеширования React Query
- Оптимизация производительности (мемоизация, профилирование)

**Спринт 3 (тестирование и полировка) — 2–3 дня**
- Модульные тесты
- Интеграционные тесты
- Документация

## Чеклист приемки

- [ ] Созданы и используются TypeScript-модели
- [ ] Реализованы хуки React Query
- [ ] Поток бронирования проходит серверную валидацию
- [ ] Соблюдаются окна отмены
- [ ] Интерфейс календаря с состояниями слотов и модальным окном
- [ ] Панель администратора для управления тренерами/слотами
- [ ] Реализовано аудит-логирование
- [ ] Настроены ESLint + Prettier
- [ ] Профилирование производительности (скриншоты до/после оптимизаций)

## Deliverables (что будет проверено)

 Logs audit implemented

 ESLint + Prettier + minimal tests

 Производительность: скриншоты профилировщика, показывающие меньше рендеров после мемоизации (для представления во время ревью)

## Deliverables (что будет проверено)

- GitHub repository with:
  - frontend in `client/`
  - backend in `server/` (if implemented)
  - README with setup/run instructions, env vars, migration steps
  - Postman collection or OpenAPI spec (optional)
  - Mini-spec (Markdown) + diagrams (ER, component tree) — included in PR
  - Demo link (optional; local run instructions suffice)
  - Short writeup: what optimizations were made and why

## Критерии оценки (при проверке репозитория)

- "Корректность бизнес-логики (бронирование/отмена)"

- "Чистота архитектуры (hooks, separation of concerns)"

- "Наличие оптимизаций ререндеров и объяснение/демонстрация (profiler logs)"

- "Error handling + toast UX"

- "Тесты на основные сценарии (post-MVP requirement)"

- "Readme & setup ease"