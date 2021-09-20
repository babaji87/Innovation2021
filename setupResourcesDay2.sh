echo "########################## "
echo -e  " Set Shell variables"
echo "######################### "
VERSION=4
SUBSCRIPTION="e247041b-0729-4095-9488-564fbc84a3b7"
RESOURCE_GROUP="default"
AKS_CLUSTER_NAME="anjnaaks"$VERSION
AKS_VNET_SUBNET="default-"$VERSION
ACR_REG_NAME="anjnaacr"$VERSION
POSTGRES_USER_NAME="anjna"$VERSION
AKS_ROUTE_TABLE="anjna_aks_route_table"$VERSION
POSTGRES_DB=anjnapostgres$VERSION
LOCATION="eastus"
SKU="Basic"
SERVICEBUSNAME="anjnasb"$VERSION
SERVICEBUSQUEUE="anjnaq"$VERSION
KEYVAULTNAME="anjnakv"$VERSION
POSTGRES_PASSWD="Postgres"$VERSION
SERVICE_PRINCIPAL="anjnasp"$VERSION
SP_TENANT_ID="8d09f28d-2b54-4761-98f1-de38762cd939"
echo "########################## "
echo -e  " Create Postgres Server"
echo "######################### "
sudo az account set -s $SUBSCRIPTION
sudo az postgres server create --resource-group $RESOURCE_GROUP  --name $POSTGRES_DB --ssl-enforcement Disabled \
    --location eastus --admin-user $POSTGRES_USER_NAME --admin-password  $POSTGRES_PASSWD --sku-name GP_Gen5_2
sudo az postgres server firewall-rule create \
    --subscription $SUBSCRIPTION \
    --resource-group $RESOURCE_GROUP \
    --server $POSTGRES_DB \
    --name AllowMyIP \
    --start-ip-address 0.0.0.0 \
    --end-ip-address 255.255.255.255
    
echo "########################## "
echo -e  " Create Container Registry "
echo "######################### "

sudo az acr create --resource-group $RESOURCE_GROUP \
  --name $ACR_REG_NAME --sku $SKU
 
 echo "########################## "
 echo -e "creating the keyvault to store and retrieve secrets in our apps"
 echo "########################## "

SP_PASSWORD=$(sudo az ad sp create-for-rbac --name $SERVICE_PRINCIPAL --query password -o tsv)
SP_APPID=$(sudo az ad sp list --display-name $SERVICE_PRINCIPAL --query [].appId -o tsv)
SP_NAME=$(sudo az ad sp list --display-name $SERVICE_PRINCIPAL --query [].name -o tsv)
sudo az keyvault create \
    --resource-group $RESOURCE_GROUP \
    --name $KEYVAULTNAME \
    --enabled-for-deployment true \
    --enabled-for-disk-encryption true \
    --enabled-for-template-deployment true \
    --location $LOCATION \
    --query properties.vaultUri \
    --sku standard

sudo az keyvault set-policy --name $KEYVAULTNAME --spn $SP_NAME --secret-permissions get list
sudo az keyvault secret set --name "connectionString" \
    --vault-name $KEYVAULTNAME \
    --value "jdbc:sqlserver://SERVER.database.windows.net:1433;database=DATABASE;"


echo "########################## "
echo -e "Creating service bus and queue for messaging"
echo "########################## "

#namespace is very similar to namespace in programming languages
# every identifier identifies a dist
#every namespace=> for a unique app
sudo az servicebus namespace create --resource-group $RESOURCE_GROUP --name $SERVICEBUSNAME --location $Location

#queue=>list of messages=>MQ
#every queue has its own list of participants=> senders and receivers \
#queue=> group chat thread on whatsapp
sudo az servicebus queue create --resource-group $RESOURCE_GROUP --namespace-name $SERVICEBUSNAME --name $SERVICEBUSQUEUE
