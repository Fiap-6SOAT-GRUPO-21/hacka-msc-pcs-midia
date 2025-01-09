@echo off
kubectl delete deployments --all -n default
kubectl delete replicasets --all -n default
kubectl delete statefulsets --all -n default
kubectl delete services --all -n default
kubectl delete pods --all -n default
kubectl delete pvc --all -n default
kubectl delete hpa --all -n default
kubectl delete pv --all
kubectl delete role --all
kubectl delete rolebinding --all
kubectl delete configmap --all
pause
