\
# Load Tests

Load tests verify the portal can handle expected traffic and identify performance bottlenecks.

## Approach

Using k6 for HTTP load testing. Test scripts are in `tests/load/`.

## Scenarios

| Scenario | VUs | Duration | Target |
|----------|-----|----------|--------|
| Normal load | 50 | 10 min | p95 < 500ms |
| Peak load | 500 | 5 min | p95 < 2s |
| Spike test | 50 → 1000 → 50 | 10 min | No errors > 1% |
| Endurance | 100 | 2 hours | No memory leak |
| Scheme day | 1000 | 30 min | p95 < 2s, 0% error |

## Running

```bash
# Normal load test
k6 run tests/load/normal.js

# Spike test
k6 run tests/load/spike.js

# Via shell script (simpler)
./scripts/load-test.sh 10m 100
```

## Metrics Collected

- Request latency (p50, p95, p99)
- Error rate (%)
- Requests per second (throughput)
- API response times by endpoint
- Database query times
- JVM heap usage
