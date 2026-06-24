\
# Runbook: RabbitMQ Broker Down

## Symptoms
- Async notifications (email/SMS) not being sent
- Search indexing delayed
- Export generation jobs queued but not processing

## Impact
No immediate user-facing impact (portal remains functional). Delayed notifications and stale search results.

## Diagnosis

```bash
# Check RabbitMQ pod
kubectl get pods -n avgcxr-prod -l app=avgcxr-rabbitmq

# Check RabbitMQ logs
kubectl logs -l app=avgcxr-rabbitmq -n avgcxr-prod --tail=100

# Check queue status
kubectl exec -it <rmq-pod> -- rabbitmqctl list_queues name messages

# Check cluster status (if applicable)
kubectl exec -it <rmq-pod> -- rabbitmqctl cluster_status
```

## Mitigation

### RabbitMQ OOM
```bash
# Check memory
kubectl exec -it <rmq-pod> -- rabbitmqctl status | grep -A5 memory

# Purge old messages from dead letter queue
kubectl exec -it <rmq-pod> -- rabbitmqctl purge_queue avgcxr.dead

# Increase memory watermark
kubectl exec -it <rmq-pod> -- rabbitmqctl set_vm_memory_high_watermark 0.7
```

### Connection Refused
```bash
# Restart RabbitMQ
kubectl rollout restart deployment avgcxr-rabbitmq -n avgcxr-prod

# Restart API and worker to reconnect
kubectl rollout restart deployment avgcxr-api -n avgcxr-prod
```

## Prevention
- Monitor queue depth (alert at 10,000 messages)
- Set message TTL to prevent queue bloat
- Regular dead letter queue cleanup
