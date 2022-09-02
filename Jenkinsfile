node {
      stage('init') {
        checkout scm
      }
      stage('docker build') {
        echo '---------docker build 시작------------'
        sh 'docker build --build-arg ENVIRONMENT=docker -t  gateway-dev .'
        sh 'docker tag gateway-dev mungtaregistry.azurecr.io/mungta/dev/gateway'
      }
      stage('deploy') {
        withCredentials([azureServicePrincipal('azure_service_principal')]) {
          // Log in to Azure
          echo '---------az login------------'
          sh '''
            az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET -t $AZURE_TENANT_ID
            az account set -s $AZURE_SUBSCRIPTION_ID
          '''
          //ACR AKS 연결
          echo '---------ACR AKS 연결------------'
          sh 'az acr login --name mungtaregistry'
          sh 'az aks get-credentials --resource-group devops-rg --name mungta-kubernetes --overwrite-existing'

          echo '---------AKS 배포------------'
          sh 'docker push mungtaregistry.azurecr.io/mungta/dev/gateway'
          sh 'kubectl apply -f ./kubernetes/deployment.yml'
          sh 'kubectl apply -f ./kubernetes/service.yml'
          sh 'kubectl rollout restart deployment gateway --namespace=mungta'

          sh 'az logout'
        }
      }

      stage('Clear') {
          echo '---------이미지 제거------------'
          sh "docker rmi \$(docker images -f 'dangling=true' -q)"
      }
    }
