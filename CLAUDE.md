# Senior Software Engineer Interview Prep — Master Context

## Who I am & what I'm doing
I am preparing for Senior Software Engineer roles at **Amazon, Capital One, BlackRock, and Pinterest**.
I have a 16-week structured preparation plan divided into 4 phases.
This file is the single source of truth — always use it when I ask for guidance, suggestions, or next steps.

## Target Companies & Key Notes
| Company | Key Focus |
|---|---|
| Amazon | Leadership Principles in every round — carry same weight as technical |
| Capital One | Financial domain expertise + Power Day format (multiple rounds same day) |
| BlackRock | Rigour, data integrity, correctness over speed |
| Pinterest | Scale, product thinking, feed/recommendation systems |

---

## The Project: OrderFlow
A production-grade order processing system built with **Spring Boot + PostgreSQL + Kafka + AWS**.

**Order state machine:**
`PENDING → INVENTORY_RESERVED → CONFIRMED | REJECTED | PAYMENT_FAILED | CANCELLED`

**Core tables:** `orders`, `order_items`, `order_status_history`, `payments`, `payment_attempts`, `inventory`

**Key architectural decisions made:**
- Transactional Outbox Pattern for reliable Kafka publishing
- Resilience4j for circuit breaker + retry with exponential backoff + jitter
- Kafka DLQ after 3 failed retries
- Compensation flows: `PAYMENT_REJECTED` → publish `INVENTORY_RELEASE_REQUESTED`
- Micrometer + Prometheus + Grafana dashboard (order throughput, failure rate, retry rate, DLQ size)
- Structured logging with Logback + MDC (correlationId, orderId injected automatically)
- Composite indexes: `(customer_id, status)` and `(created_at, status)` — justified with EXPLAIN ANALYZE
- Redis caching for hot reads
- `SELECT FOR UPDATE` for inventory reservation to prevent race conditions

**Tech stack:** Java, Spring Boot, PostgreSQL, Kafka, Resilience4j, Micrometer, Docker Compose, AWS (EC2, RDS, MSK, SQS, ALB, CloudWatch, VPC)

---

## 5 Weekly Tracks (run every week)
1. **Algorithms** — LeetCode patterns, timed practice, out-loud explanation habit
2. **Backend / DB** — Spring Boot, PostgreSQL, concurrency, observability
3. **Project — OrderFlow** — hands-on deliverable each week
4. **Cloud (AWS)** — mapped directly to OrderFlow architecture decisions
5. **Behavioral** — one Amazon Leadership Principle + one STAR story per week, practised out loud

---

## PHASE 1 — Foundations (Weeks 1–4)

### Week 1 — Arrays, sliding window + domain modelling
**Algorithms:** Sliding Window, Two Pointers — Longest Substring Without Repeating Characters, Minimum Size Subarray Sum, Two Sum II. Resource: A Common-Sense Guide Ch. 1–4.
**Backend/DB:** Relational modelling (1NF→3NF), PK/FK, B-Tree indexes. Resource: DDIA Ch. 1.
**OrderFlow deliverable:** ERD v1 finalised (`orders`, `order_items`, `order_status_history`) + Spring Boot skeleton + Docker Compose with PostgreSQL + first Flyway/Liquibase migrations. Running empty app connected to DB.
**AWS:** IaaS vs PaaS vs SaaS. Study EC2 — how to deploy a JVM app there.
**Behavioral LP:** Customer Obsession. STAR story: time you went beyond requirements to deliver value for a user/customer.

### Week 2 — Binary search, trees + REST API skeleton
**Algorithms:** Binary Search variants, BST traversals (in/pre/post order), Balanced BST concept — Binary Search, Find Minimum in Rotated Sorted Array, Validate BST, Lowest Common Ancestor.
**Backend/DB:** REST API design (idempotency, correct HTTP status codes, versioning), `@Transactional` semantics.
**OrderFlow deliverable:** `POST /orders` (creates order, reserves inventory synchronously), `GET /orders/{id}`, all error cases mapped to correct HTTP codes. Full integration tests with TestContainers.
**AWS:** Amazon RDS — managed PostgreSQL, automated backups, parameter groups, connection pooling basics.
**Behavioral LP:** Ownership. STAR story: time you took responsibility for something outside your formal scope.

### Week 3 — Heaps, priority queues + payment module
**Algorithms:** Min/Max Heap, Top-K pattern — Top K Frequent Elements, Kth Largest Element, Merge K Sorted Lists, Task Scheduler.
**Backend/DB:** `@ControllerAdvice` global exception handler, `@Valid` + custom constraints, `CompletableFuture` / `ExecutorService`.
**OrderFlow deliverable:** Payment module (`payments` + `payment_attempts` tables), configurable success rate simulation, full synchronous flow `POST /orders → reserve inventory → attempt payment → confirm or fail`. All error paths handled and tested.
**AWS:** Application Load Balancer — routing, health checks. Horizontal scaling concept. Question: what in OrderFlow breaks if you run 3 instances simultaneously?
**Behavioral LP:** Dive Deep. STAR story: time you investigated a production issue to root cause level (mention specific metrics/logs/tools).

### Week 4 — Graphs + inventory module + VPC networking
**Algorithms:** BFS (shortest path), DFS (connectivity), Topological Sort (Kahn's + DFS) — Number of Islands, Course Schedule I & II, Clone Graph, Pacific Atlantic Water Flow. Resource: Algorithm Design Manual Ch. 5.
**Backend/DB:** Optimistic vs pessimistic locking, `SELECT FOR UPDATE` for inventory reservation, race condition analysis.
**OrderFlow deliverable:** Inventory module with `SELECT FOR UPDATE` reservation, idempotent order creation (unique constraint + conflict handling), optimistic locking version column. Demonstrated: two concurrent requests for last unit — one succeeds, one gets 409.
**AWS:** VPC — public vs private subnets, security groups, NAT Gateway. Where each OrderFlow component lives in a VPC.
**Behavioral LP:** Bias for Action. STAR story: time you made a decision and moved forward with incomplete information.

---

## PHASE 2 — Backend & Events (Weeks 5–8)

### Week 5 — Kafka fundamentals + async order flow
**Algorithms:** Linked Lists (slow/fast pointer, reversal, cycle detection) — Reverse Linked List, Linked List Cycle, LRU Cache, Merge Two Sorted Lists.
**Backend/DB:** Kafka fundamentals — topic, partition, offset, consumer group, at-least-once vs exactly-once. When Kafka over REST.
**OrderFlow deliverable:** Kafka integrated via Docker Compose. `PAYMENT_COMPLETED` / `PAYMENT_REJECTED` events. Async flow: HTTP call returns after inventory reserved, Kafka consumer handles payment result and updates order status. Integration test verifies full async flow.
**AWS:** Amazon MSK vs self-managed Kafka. Why MSK for OrderFlow. MSK vs SQS trade-off for this use case.
**Behavioral LP:** Deliver Results. STAR story: time you delivered under a tight deadline or pressure.

### Week 6 — Dynamic programming + modular architecture + DB indexing
**Algorithms:** 1D DP (memoisation + tabulation), 2D DP, DP on strings — Climbing Stairs, House Robber, Longest Common Subsequence, Coin Change, Word Break. Process: always define state → transition → base case before writing code. Resource: A Common-Sense Guide Ch. 12.
**Backend/DB:** EXPLAIN ANALYZE (read actual cost, rows, loops), partial indexes, covering indexes, transactions / isolation levels (READ COMMITTED vs REPEATABLE READ vs SERIALIZABLE). Resource: DDIA Ch. 7.
**OrderFlow deliverable:** `GET /orders?customerId=&status=&from=&to=` search endpoint. Composite indexes `(customer_id, status)` and `(created_at, status)`. EXPLAIN ANALYZE run and documented.
**AWS:** RDS Read Replicas — which queries benefit. Multi-AZ — what it protects against.
**Behavioral LP:** Invent and Simplify. STAR story: time you simplified a complex process (not just adopting a framework — reducing complexity for the team).

### Week 7 — Backtracking, tries + Outbox pattern
**Algorithms:** Backtracking (state-space search, pruning), Trie (prefix tree) — Subsets, Permutations, N-Queens, Word Search, Implement Trie, Word Search II.
**Backend/DB:** Transactional Outbox Pattern — why it solves dual-write problem, how the relay/poller works, idempotency on consumer side.
**OrderFlow deliverable:** Outbox pattern implemented. `outbox_events` table in same transaction as order update. Separate poller publishes to Kafka. Consumer idempotency via processed event IDs. Test: DB commit succeeds but Kafka is down — event still delivered when Kafka recovers.
**AWS:** Amazon SQS — standard vs FIFO, visibility timeout, DLQ configuration. When SQS over Kafka for OrderFlow.
**Behavioral LP:** Are Right, A Lot. STAR story: time you made a technically correct call that others initially doubted.

### Week 8 — System design foundations + Redis caching
**Algorithms:** Intervals + sorting algorithms — Merge Intervals, Meeting Rooms II, Non-overlapping Intervals. Sort algorithms complexity and when each is used. Resource: A Common-Sense Guide Ch. 15–16.
**Backend/DB:** Caching strategies — cache-aside, write-through, write-behind. Cache invalidation. Redis data structures (String, Hash, List, Set, Sorted Set).
**OrderFlow deliverable:** Redis via Docker Compose. Cache order status (`GET /orders/{id}`) with 60s TTL. Invalidate on status change. Test: first call hits DB, second hits cache, status update invalidates, next call re-fetches.
**AWS:** Amazon ElastiCache (Redis) — cluster mode, eviction policies, when to use vs DynamoDB for hot read path.
**System Design:** Design a URL Shortener end-to-end. Cover: hashing strategy, collision handling, storage, redirects at scale, analytics.
**Behavioral LP:** Think Big. STAR story: time you proposed or executed a solution larger in scope than what was asked.

---

## PHASE 3 — Senior Depth (Weeks 9–12)

### Week 9 — Advanced graphs + system design: rate limiter
**Algorithms:** Advanced graph algorithms — Dijkstra, Bellman-Ford (when each), Union-Find / DSU. Problems: Cheapest Flights Within K Stops, Network Delay Time, Number of Connected Components.
**Backend/DB:** Distributed systems concepts — CAP theorem (C vs A in partition), eventual consistency, vector clocks, read-your-writes. Resource: DDIA Ch. 9.
**OrderFlow deliverable:** `GET /orders/customer/{id}/summary` — paginated with cursor-based pagination. EXPLAIN ANALYZE documented. Load test: simulate 500 concurrent reads, measure p99 latency.
**AWS:** Amazon CloudFront — edge caching, cache-control headers, when CDN helps OrderFlow.
**System Design:** Rate Limiter — token bucket vs sliding window log vs sliding window counter. Redis-based implementation. Trade-offs at distributed scale.
**Behavioral LP:** Insist on the Highest Standards. STAR story: time you raised the bar and pushed back on a solution you considered insufficient.

### Week 10 — Advanced trees + system design: distributed cache
**Algorithms:** Advanced trees — Segment Tree (range queries), AVL/Red-Black concepts, Morris Traversal. Problems: Range Sum Query, Count of Smaller Numbers After Self, Binary Tree Maximum Path Sum.
**Backend/DB:** Event sourcing — event log as source of truth, projections, snapshotting. CQRS — separate read/write models. When each pattern makes sense.
**OrderFlow deliverable:** Event sourcing for order status history — `order_events` table stores every transition event. Query: full audit trail for any order. Projection: rebuild current state from events. Document trade-offs vs current status column approach.
**AWS:** DynamoDB — partition key design, hot partition problem, GSI for query patterns. Trade-off: DynamoDB vs RDS for OrderFlow given actual query patterns.
**System Design:** Distributed Cache (Redis Cluster) — consistent hashing, replication, eviction policies, handling node failure.
**Behavioral LP:** Earn Trust. STAR story: time you delivered bad news to a stakeholder and maintained/rebuilt trust (include what you changed in your process to prevent recurrence).

### Week 11 — Resilience — retries, DLQ, circuit breakers
**Algorithms:** Hard problem week — pick 3 hard LeetCode in your weakest pattern. Solve one completely out loud explaining every decision.
**Backend/DB:** Retry strategies (fixed, exponential backoff, jitter — why jitter prevents thundering herd). Circuit breaker states: closed → open → half-open. Bulkhead (thread pool isolation). DLQ: what goes there, monitoring, safe replay. Resource: DDIA Ch. 8 + Resilience4j docs.
**OrderFlow deliverable:** Resilience4j circuit breaker on payment simulation + retry with exponential backoff + jitter. Compensation flow: `PAYMENT_REJECTED` → publish `INVENTORY_RELEASE_REQUESTED` → consumer releases reservation. Kafka DLQ after 3 failed retries with failure reason header. Test: force payment failure 3× → verify DLQ receives message, inventory released, order status `PAYMENT_FAILED`.
**AWS:** SQS DLQ configuration — max receive count, visibility timeout interaction with retries. Multi-AZ: what it gives vs doesn't (failover time, consistency during failover).
**System Design:** Design OrderFlow for 10x traffic spikes — auto-scaling, SQS buffering, RDS connection pooling via RDS Proxy, cache warming, circuit breakers.
**Behavioral LP:** Have Backbone; Disagree and Commit. STAR story: you strongly disagreed with a decision — how you argued your position and what you did after. Show you can commit fully when overruled.

### Week 12 — Observability — metrics, logs, Grafana
**Algorithms:** Review + speed — revisit 5 weakest problems from weeks 1–11, solve again timed. Speed drill: 3 mediums in 25 minutes. One full 45-min coding mock (evaluate communication as much as correctness).
**Backend/DB:** Structured logs (JSON, required fields: timestamp, level, correlationId, orderId, service). Metrics types: counters, gauges, histograms. Tracing concepts: trace ID, span, parent span. SLO definition (e.g. p99 order creation < 500ms). Resource: DDIA Ch. 12.
**OrderFlow deliverable:** Micrometer counters for `orders.created`, `orders.confirmed`, `orders.failed`, `retries.total`, `dlq.messages`. Histogram for `order.processing.duration` (creation → CONFIRMED). Logback + MDC: correlationId and orderId injected automatically. `/actuator/prometheus` endpoint. Prometheus + Grafana in Docker Compose. Dashboard: order throughput, failure rate, retry rate, DLQ size.
**AWS:** CloudWatch — log groups, metrics, alarms, dashboards. Map Prometheus metrics → CloudWatch custom metrics in production. Define 3 production alerts with thresholds.
**Behavioral LP:** Learn and Be Curious. STAR story: time you proactively learned something outside your role and applied it to solve a real problem. Plus: second full behavioral mock (4 LPs, timed, no notes — record yourself).

---

## PHASE 4 — Interview Simulation (Weeks 13–16)

### Week 13 — Coding intensive + system design: feeds & search
**Algorithms:** 8–10 problems this week, medium/hard mix, all timed. 2 full coding mocks (45 min each) via Pramp or interviewing.io. After each mock: write what you explained poorly, not just what you got wrong.
**System Design:** News Feed (Pinterest-like) — follows, ranked timeline, fan-out on write vs read. Typeahead/autocomplete — Trie in memory vs Elasticsearch, caching strategy. Go deep on one component per design.
**OrderFlow deliverable:** Write one ADR (Architecture Decision Record) for your most important design choice (e.g. outbox pattern vs saga). Write 5-minute pitch for OrderFlow (practice out loud). Identify the 3 most complex flows you must be able to explain line-by-line.
**AWS:** ECS vs EKS — container orchestration trade-offs. How you'd deploy OrderFlow to production on AWS with blue/green deployment.
**Behavioral LP:** Frugality. STAR story: time you achieved a significant result with limited resources or budget.

### Week 14 — Full mock interview week
**Algorithms:** Maintenance mode — 4–5 problems per day. For every problem solved in weeks 1–13: can you explain the intuition in 30 seconds?
**System Design:** Full system design mocks — Amazon-level depth expected. Design: ride-sharing dispatch, financial ledger (double-entry, eventual consistency), distributed message queue.
**OrderFlow deliverable:** Full code quality pass — remove dead code, ensure all error paths handled, add missing tests. Write README: architecture overview, how to run locally, key design decisions, known limitations.
**AWS:** Mock question: "Your order service sees 10x traffic spikes during flash sales — walk me through how you handle this in AWS." Answer must cover: auto-scaling, SQS buffering, RDS Proxy, cache warming, circuit breakers.
**Behavioral:** Full Amazon loop mock — 5 rounds × 2 LPs each with a peer as interviewer. Capital One: prepare for Power Day format (multiple back-to-back rounds, same day). Identify remaining gaps.

### Week 15 — Final simulation + company-specific prep
**Algorithms:** Mock in interview conditions — no IDE hints, talk through every decision, 45 min hard limit per problem.
**System Design:** Final design reviews — draw each design from weeks 8–14 from memory without notes. Pick your most fluent design as your opening story for "tell me about a complex system you've worked on."
**OrderFlow deliverable:** Rehearse: 5-minute pitch AND 10-minute deep dive version. Prepare answers to: "what would you change?", "how does this scale to 10x?", "what was the hardest problem you solved?". Know every line and every decision.
**AWS:** Review AWS architecture diagram — explain every component and why chosen over alternatives. Prepare 3 cost/trade-off decisions baked into your architecture.
**Company-specific prep:**
- Amazon: LPs in every round, non-negotiable — run through all 16 stories
- Capital One: financial domain + delivery focus
- BlackRock: rigour + data integrity above all
- Pinterest: scale + product thinking + feed/recommendation systems

### Week 16 — Final polish — ready to apply
**Algorithms:** Maintenance — 3–4 problems per day. No new patterns, only consolidation.
**System Design:** Final review of all designs. Know your strongest one cold.
**OrderFlow:** Interview-ready. Know every line and every decision. 5-min pitch + 10-min deep-dive both polished.
**AWS:** Every component and its trade-off justified.
**Behavioral:** Final story review — 16 STAR stories, one per week, no two overlapping significantly. Tailor top 5 per company.
**Milestone: Submit applications to Amazon, Capital One, BlackRock, and Pinterest.**

---

## Amazon Leadership Principles — Weekly Assignment
| Week | LP |
|---|---|
| 1 | Customer Obsession |
| 2 | Ownership |
| 3 | Dive Deep |
| 4 | Bias for Action |
| 5 | Deliver Results |
| 6 | Invent and Simplify |
| 7 | Are Right, A Lot |
| 8 | Think Big |
| 9 | Insist on the Highest Standards |
| 10 | Earn Trust |
| 11 | Have Backbone; Disagree and Commit |
| 12 | Learn and Be Curious |
| 13 | Frugality |
| 14 | Hire and Develop the Best |
| 15 | Strive to be Earth's Best Employer |
| 16 | Full loop simulation — all LPs reviewed |

---

## Resources
- A Common-Sense Guide to Data Structures and Algorithms
- Designing Data-Intensive Applications (DDIA) — Martin Kleppmann
- Algorithm Design Manual
- LeetCode (medium/hard focus from week 5 onward)
- Pramp / interviewing.io for mock interviews
- AWS Documentation (EC2, RDS, MSK, SQS, ALB, ElastiCache, CloudWatch, VPC, DynamoDB, ECS/EKS)
- Resilience4j documentation

---

## My Profile & Working Style
- **Availability:** 3–4 hours per day
- **Strong:** Spring Boot feels natural
- **Weak spot:** Database (indexing, query performance, transactions, locking) — go deeper here when relevant
- **Kafka/async:** New territory — explain concepts before jumping to implementation
- **AWS:** Needs reinforcement — always tie AWS concepts to OrderFlow, never generic theory

---

## Current Progress

### Week 2 — IN PROGRESS
- ✅ `POST /orders` API implemented
- ❌ `order_status_history` not yet implemented — **must be finished before moving to week 3**
- ❌ `Idempotency-Key` header on `POST /orders` — **must be finished before moving to week 3**
- ❌ Integration tests with TestContainers for the full happy path

### Important correction on order_status_history
The CLI previously suggested deferring this because "there is no previous status on creation." That is wrong. The first row in `order_status_history` IS the creation event — when an order is created, insert a `PENDING` row in the same transaction. The table is an append-only audit log, not a diff table. Example:
```
order_id | status             | changed_at          | reason
-------- | ------------------ | ------------------- | ------
abc-123  | PENDING            | 2026-05-25 10:00:00 | Order created
abc-123  | INVENTORY_RESERVED | 2026-05-25 10:00:01 | Inventory reserved
abc-123  | CONFIRMED          | 2026-05-25 10:00:03 | Payment successful
```
This matters especially for Capital One and BlackRock — financial companies expect a complete immutable audit trail from the moment of creation. A missing creation row is a red flag in those interviews.

---

## Approved Improvements to OrderFlow
These were discussed and agreed upon. Integrate them at the right week — do not ignore them.

### 1. Idempotency-Key header on `POST /orders` — Week 2 (NOW)
A proper `Idempotency-Key` header pattern: store the key + response in an `idempotency_keys` table, return the cached response on duplicate requests. This is what Capital One and BlackRock specifically probe. It also directly exercises the DB weak spot (schema design, unique constraints, transaction handling).

### 2. Testcontainers for Kafka — Week 5 (when Kafka is introduced)
Use Testcontainers for Kafka from day one in week 5 instead of an embedded broker. Never retrofit — add it when Kafka is first integrated so all integration tests are production-representative from the start.

### 3. OpenTelemetry (distributed tracing) — Week 12 (alongside observability)
Replace the manual MDC tracing with OpenTelemetry. Add OTEL alongside Micrometer + Grafana in week 12. This gives distributed traces that tie a single request across HTTP → Kafka → consumer into one trace with spans. Key interview talking point: "how do you debug a slow order that touched 4 services?" Strong senior signal.

### 4. Saga Orchestrator Design Doc — Week 10 (alongside event sourcing)
Not a full implementation — a written ADR comparing:
- Choreography (what OrderFlow currently uses — events triggering other events)
- Orchestration (what Temporal or a simple state machine would look like)
Being able to argue the trade-offs between both is what separates senior candidates. Write this when already deep in event-driven thinking during week 10.

### 5. ECS/EKS — Simplified scope (Week 13)
Deploy OrderFlow on ECS Fargate (straightforward). Know the trade-offs conceptually. Deep EKS knowledge is a distraction for a backend engineering role — be able to explain when you'd move to EKS and why, but don't go deeper than that.

---

## Architecture Flexibility
The plan and OrderFlow architecture are a strong baseline, not a rigid contract. When suggesting improvements:
- Point out if something in the current design could be done better
- Explain the trade-off clearly (complexity added vs interview value gained)
- Help assess whether it fits within the current week's time budget (3–4 hrs/day)
- Flag if an improvement is better deferred to a later week
- Never silently drop an approved improvement — if it gets deferred, note the new target week

---

## How to Use This Context
When I ask for help with interview prep, always:
1. **Check current progress first** — reference where I am (currently week 2, in progress) and flag anything that must be completed before advancing
2. **Respect the DB weak spot** — go deeper on database concepts, don't assume I know indexing, locking, or transaction semantics
3. **Tie AWS to OrderFlow** — never explain AWS services in the abstract; always anchor to a specific OrderFlow component
4. **Remind behavioral** — if it's a new week, remind me to write and practise the STAR story out loud (no notes)
5. **Suggest proactively** — if you see a better approach than what's in the plan, say so with a clear trade-off explanation and a feasibility assessment given 3–4 hrs/day
6. **Senior depth** — I'm targeting Senior/Staff roles; don't over-explain basics unless I ask, but do go deep on trade-offs and design decisions
7. **Never contradict the order_status_history correction above** — the first row is always the PENDING creation event
