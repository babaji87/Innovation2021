echo "########################## "
echo -e  " Set Shell variables"
echo "######################### "
VERSION=2
SUBSCRIPTION="e247041b-0729-4095-9488-564fbc84a3b7"
RESOURCE_GROUP="default"
AKS_CLUSTER_NAME="anjnaaks"$VERSION
AKS_VNET_SUBNET="default-"$VERSION
ACR_REG_NAME="anjnaacr"
ACR_REG_NEW_NAME="anjnaacr"$VERSION
AKS_ROUTE_TABLE="anjna_aks_route_table"$VERSION
echo "########################## "
echo -e  " Create docker images and push them to ACR "
echo "######################### "
sudo docker-compose up --no-start
sudo docker tag anjnadockerid1/stocksserverfrontend:v3 $ACR_REG_NEW_NAME.azurecr.io/stocksserverfrontend:v3
sudo docker tag anjnadockerid1/stocksserverbackend:v3 $ACR_REG_NEW_NAME.azurecr.io/stocksserverbackend:v3
sudo docker tag anjnadockerid1/stocksserverworker:v3 $ACR_REG_NEW_NAME.azurecr.io/stocksserverworker:v3
sudo az acr login --name $ACR_REG_NEW_NAME
sudo docker push $ACR_REG_NEW_NAME.azurecr.io/stocksserverfrontend:v3
sudo docker push $ACR_REG_NEW_NAME.azurecr.io/stocksserverbackend:v3
sudo docker push $ACR_REG_NEW_NAME.azurecr.io/stocksserverworker:v3
sudo az acr repository list --name  $ACR_REG_NEW_NAME --output table
echo "########################## "
echo -e  " Create AKS"
echo "######################### "
sudo az network route-table create --subscription $SUBSCRIPTION --name $AKS_ROUTE_TABLE --resource-group infrastructure
sudo az network route-table route create --subscription $SUBSCRIPTION --route-table-name $AKS_ROUTE_TABLE --resource-group infrastructure --name default-route --address-prefix 0.0.0.0/0 --next-hop-type VirtualNetworkGateway
sudo az network vnet subnet update --subscription $SUBSCRIPTION --resource-group infrastructure --vnet-name default --name $AKS_VNET_SUBNET --route-table $AKS_ROUTE_TABLE
sudo az aks create --subscription $SUBSCRIPTION --resource-group $RESOURCE_GROUP --name $AKS_CLUSTER_NAME --outbound-type userDefinedRouting --network-plugin azure --generate-ssh-keys --vnet-subnet-id /subscriptions/$SUBSCRIPTION/resourceGroups/infrastructure/providers/Microsoft.Network/virtualNetworks/default/subnets/$AKS_VNET_SUBNET
SP_ID=$(sudo az resource list --subscription $SUBSCRIPTION --resource-group $RESOURCE_GROUP --name $AKS_CLUSTER_NAME --query [*].identity.principalId -o tsv)
sudo az role assignment create --assignee $SP_ID --role "Contributor" --scope /subscriptions/$SUBSCRIPTION/resourceGroups/infrastructure
sudo az aks get-credentials --subscription $SUBSCRIPTION --resource-group $RESOURCE_GROUP --name $AKS_CLUSTER_NAME
sudo az aks update -n $AKS_CLUSTER_NAME -g $RESOURCE_GROUP --attach-acr $ACR_REG_NEW_NAME
echo "########################## "
echo -e  " Creation complete "
echo "######################### "
sudo kubectl get nodes
sudo az aks get-credentials --resource-group $RESOURCE_GROUP --name $AKS_CLUSTER_NAME
sudo kubectl get nodes
egrep -lRZ $ACR_REG_NAME | xargs -0 -l sed -i -e s/$ACR_REG_NAME/$ACR_REG_NEW_NAME/g
sudo kubectl apply -f k8s
