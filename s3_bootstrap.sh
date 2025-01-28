# Configure a AWS CLI para o Localstack
# A AWS CLI precisa ser configurada para apontar para o Localstack. Siga as etapas abaixo:
# Adicionar uma configuração de perfil personalizado (opcional): Você pode criar um perfil específico para o Localstack:

aws configure --profile localstack


#Insira as credenciais (que o Localstack aceita de forma fictícia):
# Access Key ID: test
# Secret Access Key: test
# Default Region: us-east-1

aws s3api create-bucket \
  --bucket media-files \
  --endpoint-url http://localhost:4566 \
  --region us-east-1