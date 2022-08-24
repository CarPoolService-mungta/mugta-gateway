## Mungta API Gateway

- Maven 빌드

  - ```sh
    mvn package
    ```

- Docker Image 빌드

  - ```-t```: tag 옵션, 'name:tag'. 형식

  - ```
    docker build -t mungtaregistry.azurecr.io/mungta/dev/gateway:latest .
    ```

- 레지스트리에 이미지 push

  - ```
    docker push mungtaregistry.azurecr.io/mungta/dev/gateway:latest
    ```

- Azure AKS에 배포

  - ```sh
    cd kubernetes
    kubectl apply -f deployment.yml
    kubectl apply -f service.yml
    ```

- 배포되었는지 확인

  - ```sh
    kubectl get all -n mungta
    ```

- 로그 확인

  - ```sh
    kubectl logs {pod명} -n mungta
    ```

- gateway 삭제

  - ```sh
    kubectl delete deploy gateway -n mungta
    kubectl delete svc gateway -n mungta
    
    or
    
    kubectl delete -f deployment.yml
    kubectl delete -f service.yml
    ```

- 추후에 latest가 아닌 v1, v2로 태그 설정.
