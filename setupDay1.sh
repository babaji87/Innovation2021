echo "########################## "
echo -e  " Set Shell variables"
echo "######################### "

SUBSCRIPTION="e247041b-0729-4095-9488-564fbc84a3b7"

RESOURCE_GROUP="default"

AKS_CLUSTER_NAME="anjnaaks"

AKS_VNET_SUBNET="default-4"

ACR_REG_NAME="anjnaacr"

AZ_POSTGRESQL_NAME="anjnapostgres"

AKS_ROUTE_TABLE="anjna_aks_route_table"

echo "########################## "
echo -e  " Create Postgres Server"
echo "######################### "

az postgres server create --resource-group $RESOURCE_GROUP  --name $AZ_POSTGRESQL_NAME --ssl-enforcement Disabled   --location eastus --admin-user anjnak --admin-password  Postgres1 --sku-name GP_Gen5_2
sudo az postgres server firewall-rule create \
    --subscription $SUBSCRIPTION \
    --resource-group $RESOURCE_GROUP \
    --server $AZ_POSTGRESQL_NAME \
    --name AllowMyIP \
    --start-ip-address 0.0.0.0 \
    --end-ip-address 255.255.255.255
    
echo "########################## "
echo -e  " Create Container Registry "
echo "######################### "

az acr create --resource-group $RESOURCE_GROUP \
  --name $ACR_REG_NAME --sku Basic
  echo "########################## "
echo -e  " Create docker images and push them to ACR "
echo "######################### "
sudo docker-compose up --no-start
sudo docker tag anjnadockerid1/stocksserverfrontend:v3 $ACR_REG_NAME.azurecr.io/stocksserverfrontend:v3
sudo docker tag anjnadockerid1/stocksserverbackend:v3 $ACR_REG_NAME.azurecr.io/stocksserverbackend:v3
sudo docker tag anjnadockerid1/stocksserverworker:v3 $ACR_REG_NAME.azurecr.io/stocksserverworker:v3
sudo az acr login --name $ACR_REG_NAME
sudo docker push $ACR_REG_NAME.azurecr.io/stocksserverfrontend:v3
sudo docker push $ACR_REG_NAME.azurecr.io/stocksserverbackend:v3
sudo docker push $ACR_REG_NAME.azurecr.io/stocksserverworker:v3
sudo az acr repository list --name  $ACR_REG_NAME --output table
echo "########################## "
echo -e  " Create AKS"
echo "######################### "
sudo az network route-table create --subscription $SUBSCRIPTION --name $AKS_ROUTE_TABLE --resource-group infrastructure
sudo az network route-table route create --subscription $SUBSCRIPTION --route-table-name $AKS_ROUTE_TABLE --resource-group infrastructure --name default-route --address-prefix 0.0.0.0/0 --next-hop-type VirtualNetworkGateway
sudo az network vnet subnet update --subscription $SUBSCRIPTION --resource-group infrastructure --vnet-name default --name $AKS_VNET_SUBNET --route-table $AKS_ROUTE_TABLE
sudo az aks create --subscription $SUBSCRIPTION --resource-group $RESOURCE_GROUP --name $AKS_CLUSTER_NAME --outbound-type userDefinedRouting --network-plugin azure --generate-ssh-keys --vnet-subnet-id /subscriptions/$SUBSCRIPTION/resourceGroups/infrastructure/providers/Microsoft.Network/virtualNetworks/default/subnets/$AKS_VNET_SUBNET
SP_ID=$(az resource list --subscription $SUBSCRIPTION --resource-group $RESOURCE_GROUP --name $AKS_CLUSTER_NAME --query [*].identity.principalId -o tsv)
sudo az role assignment create --assignee $SP_ID --role "Contributor" --scope /subscriptions/$SUBSCRIPTION/resourceGroups/infrastructure
sudo az aks get-credentials --subscription $SUBSCRIPTION --resource-group $RESOURCE_GROUP --name $AKS_CLUSTER_NAME
sudo az aks update -n $AKS_CLUSTER_NAME -g $RESOURCE_GROUP --attach-acr $ACR_REG_NAME
echo "########################## "
echo -e  " Creation complete "
echo "######################### "
sudo kubectl get nodes
sudo az aks get-credentials --resource-group $RESOURCE_GROUP --name $AKS_CLUSTER_NAME
sudo kubectl get nodes
cd Step3
sudo kubectl apply -f k8s
