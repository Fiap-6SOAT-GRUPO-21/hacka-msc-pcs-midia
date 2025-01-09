@echo off
kubectl apply -f ../postgres-pv.yaml
kubectl apply -f ../postgresql-pvc.yaml
kubectl apply -f ../postgresql-statefulset.yaml
kubectl apply -f ../postgresql-service.yaml
kubectl apply -f ../app-configmap.yaml
kubectl apply -f ../app-deployment.yaml
kubectl apply -f ../app-service.yaml
kubectl apply -f ../app-hpa.yaml
kubectl apply -f ../metrics.yaml
kubectl apply -f ../role.yaml
kubectl apply -f ../rolebinding.yaml
pause
