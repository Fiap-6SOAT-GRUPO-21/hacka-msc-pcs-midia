# kubernetes

## Detalhes de como rodar o projeto localmente

### Buildar a imagem do projeto

1. Buildar a imagem do projeto

```bash
docker build -t hacka-msc-pcs-midia:latest .
```

2. Verficar se a imagem foi criada corretamente

```bash
docker images | grep hacka-msc-pcs-midia
```

3. Caso queira deletar a imagem

```bash
docker rmi hacka-msc-pcs-midia:latest
```

4. Subir a imagem no Docker Hub

```bash
docker tag hacka-msc-pcs-midia:latest josevitordurante/hacka-msc-pcs-midia:latest
docker push josevitordurante/hacka-msc-pcs-midia:latest
```

## Executando a Aplicação Localmente com Minikube

Para implantar a aplicação localmente usando Minikube, você precisa aplicar os arquivos de configuração do Kubernetes
YAML. Siga estas etapas:

1. Certifique-se de ter o `minikube` e o `kubectl` instalados na sua máquina.

2. Inicie o Minikube:

```bash
minikube start
```

3. Depois de rodar o comando para subir todos os recursos, rodar o comando abaixo criar um tunnel de comunicação entre o
   seu localhost e o cluster do minikube

```bash
minikube tunnel
```

## Executando a Aplicação no EKS

Para implantar a aplicação no EKS, você precisa aplicar o arquivo de configuração do Kubernetes YAML. Siga estas etapas:

1. Certifique-se de ter o `kubectl` instalado e configurado para interagir com seu cluster EKS. Você pode fazer isso
   instalando o AWS CLI e EKS CLI (`eksctl`), e
   executando `aws eks update-kubeconfig --region <região> --name <nome-do-cluster>`.

2. Aplique o arquivo de configuração do Kubernetes dentro da pasta user/.aws/credentials:

#### Postgres

1. Rodar o pvc do Postgres

```bash
kubectl apply -f postgresql-pvc.yaml
```

2. Rodar os services do Postgres

```bash
kubectl apply -f postgresql-service.yaml
```

3. Rodar o deployment do Postgres

```bash
kubectl apply -f postgresql-statefulset.yaml
```

#### Aplicação hacka-msc-pcs-midia

1. Rodar o service da aplicação

```bash
kubectl apply -f app-service.yaml
```

2. Rodar o deployment da aplicação

```bash
kubectl apply -f app-deployment.yaml
```

#### Escalar a aplicação

```bash
kubectl apply -f app-hpa.yaml
```

#### Habilitar o metrics-server

```bash
kubectl apply -f metrics.yaml
```

### Subir tudo

```bash
kubectl apply -f postgres-pv.yaml
kubectl apply -f postgresql-pvc.yaml
kubectl apply -f postgresql-statefulset.yaml
kubectl apply -f postgresql-service.yaml
kubectl apply -f app-deployment.yaml
kubectl apply -f app-service.yaml
kubectl apply -f app-hpa.yaml
kubectl apply -f metrics.yaml
```

## Acessar a aplicação

1. Local
   http://localhost/swagger-ui/index.html#

2. Na nuvem
   http://<ip-do-node>/swagger-ui/index.html#

## Verificar se os pods estão rodando corretamente

1. Verificar os pods

```bash
kubectl get pods
```

2. Verificar os services

```bash
kubectl get services
```

3. Verificar os deployments

```bash
kubectl get deployments
```

4. Verificar os pvc

```bash
kubectl get pvc
```

## Verificar os logs do pod

1. Verificar os logs da aplicação

```bash
kubectl logs -f <nome-do-pod>
```

## Logs do InitContainer

1. Verificar os logs do initContainer, no caso verificar script de initContainer para atualizar a url de notificação do
   mercado pago utilizada pela aplicação

```bash
kubectl logs <pod-name> -c wait-for-lb
```

## Deletar os recursos

Sempre deletar os deployments, services, hpa e pvc antes de deletar os pods

```bash
kubectl delete <tipo> <nome-do-pod>
```

### Deletar tudo

```bash
kubectl delete deployments --all -n default
kubectl delete replicasets --all -n default
kubectl delete statefulsets --all -n default
kubectl delete services --all -n default
kubectl delete pods --all -n default
kubectl delete pvc --all -n default
kubectl delete hpa --all -n default
kubectl delete pv --all
```

## Buscar o IP do serviço

1. Buscar o IP do serviço

```bash
kubectl get nodes -o wide
```

2. Buscar a porta do serviço (PORT(S))

```bash
kubectl get services
```

## Testar escalabilidade

1. Comando para descrever o hpa e atualiza o consumo de CPU

```bash
kubectl describe hpa app-hpa
```

2. Comando para verificar o status do hpa

```bash
kubectl get hpa
```

3. Entrar no pod

```bash
kubectl exec -it <nome-pod>  -- /bin/bash
```

4. Dentro do pod, executar o comando para consumir a CPU

```bash
stress -c 10
```

## Utilizar ngrok para expor a aplicação localmente para a internet

1. Baixar o ngrok e crie sua conta:
   https://ngrok.com/download

2. configure o seu token

```bash
ngrok config add-authtoken <your-token>
```

3. Inicie o ngrok aponando para a porta do serviço, no log vai aparecer o link para acessar a aplicação

```bash
ngrok http http://localhost:<porta>
```