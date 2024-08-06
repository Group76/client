# Clients
Projeto para lidar com criação de clientes e gerar token.

### Arquitetura

* ECS
* DynamoDB
* SNS
* API Gateway
* ECR

Basicamente esse projeto irá gravar os clientes em um DynamoDB (NoSQL) e postar criações/alterações no tópico SNS da AWS.

Irá subir a image no ECR pela pipeline e atualizar a task do ECR.

A API Gateway irá redirecionar as chamadas para o ALB e também conta com autorização de token, para permitir somente usuários que possuem autorização.


![Diagram](https://github.com/Group76/client/blob/main/docs/client.drawio.png)

### SAGA Pattern

A pattern escolhida foi a coreografada para não ter um serviço fazendo tudo e pelo risco que tem de ele parar e o processo inteiro parar também, fora níveis de complexidade para modificações, pois quanto mais responsabilidade maior será o desafio para futuras modificações.
A coreografia foi feita utilizando o SNS, sendo assim posta as mensagens necessárias nele e lê quem tem o interesse na informação, sendo possível efetuar ações que ache necessário.

### Como rodar
Necessário subir a infraestrutura do projeto [AWS Live](https://github.com/Group76/aws-live).

Após infraestrutura configurada é só rodar a actions que a pipeline irá fazer push da image e atualizar o a task definition da ECS.
Para a pipeline funcionar na AWS necessário também configurar no github as secrets:
* AWS_ACCESS_KEY_ID
* AWS_SECRET_ACCESS_KEY

Ou se preferir rodar local é só ter configurado o mongo (pode subir usando o arquivo [docker-compose](https://github.com/Group76/client/blob/main/compose.yaml)), porém é necessário comentar os envios para o SNS.

### SWAGGER
Collection: <https://github.com/Group76/client/blob/main/docs/api-docs.json>

![Swagger](https://github.com/Group76/client/blob/main/docs/swagger-ui_index.html.png)
