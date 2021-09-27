echo "########################## "
echo -e  " Set Shell variables"
echo "######################### "
VERSION=3
SUBSCRIPTION="e247041b-0729-4095-9488-564fbc84a3b7"
RESOURCE_GROUP="default"
AKS_CLUSTER_NAME="anjnaaks"$VERSION
AKS_VNET_SUBNET="default-"$VERSION
ACR_REG_NAME="anjnaacr"$VERSION
POSTGRES_USER_NAME="anjna"$VERSION
POSTGRES_USER_NAME_ORIG="anjnak"
AKS_ROUTE_TABLE="anjna_aks_route_table"$VERSION
POSTGRES_DB=anjnapostgres$VERSION
LOCATION="eastus"
SKU="Basic"
SERVICEBUSNAME="anjnasb"$VERSION
SERVICEBUSQUEUE="anjnaq"$VERSION
KEYVAULTNAME="safevault"$VERSION
POSTGRES_PASSWD="Postgres"$VERSION
SERVICE_PRINCIPAL="anjnasp"$VERSION
SP_TENANT_ID="8d09f28d-2b54-4761-98f1-de38762cd939"
CLIENT_ORIG_IP="10.1.207.97"
 CLIENT_NEW_IP="10.1.206.97"
 SERVER_ORIG_IP="10.1.207.98"
 SERVER_NEW_IP="10.1.206.98"
 POSTGRES_ORIG_DB="anjnapostgres"
 POSTGRES_NEW_DB="anjnapostgres"$VERSION
 POSTGRES_ORIG_USER="anjnak@anjnapostgres"
 POSTGRES_NEW_USER="anjna"$VERSION"@anjnapostgres"$VERSION
 POSTGRES_ORIG_PASSWD="Postgres1"
 POSTGRES_NEW_PASSWD="Postgres"$VERSION
 SC_OLDCONNECTION="Endpoint=sb://anjnasb.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=oS39J7eYXEUUOPe9Nz8zRX1hiNpkH4FmXYSe8Glr78Y="


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
    --sku standard \
    --enable-soft-delete true 
#    --enable-purge-protection true

sudo az keyvault secret set --name "spring-datasource-url" \
    --vault-name $KEYVAULTNAME \
    --value "jdbc:postgresql://anjnapostgres"$VERSION".postgres.database.azure.com:5432/postgres"
sudo az keyvault secret set --name "spring-datasource-username" \
    --vault-name $KEYVAULTNAME \
    --value "anjna"$VERSION"@anjnapostgres"$VERSION
sudo az keyvault secret set --name "spring-datasource-password" \
    --vault-name $KEYVAULTNAME \
    --value "Postgres"$VERSION
sudo az keyvault set-policy --name $KEYVAULTNAME --spn $SP_APPID --secret-permissions get list
echo "########################## "
echo -e  " Create Postgres Server"
echo "######################### "
sudo az account set -s $SUBSCRIPTION
sudo az postgres server create --resource-group $RESOURCE_GROUP  --name $POSTGRES_DB --ssl-enforcement Disabled \
    --location eastus --admin-user $POSTGRES_USER_NAME --admin-password  $POSTGRES_PASSWD --sku-name GP_Gen5_2 --assign-identity  
sudo az postgres server firewall-rule create \
    --subscription $SUBSCRIPTION \
    --resource-group $RESOURCE_GROUP \
    --server $POSTGRES_DB \
    --name AllowMyIP \
    --start-ip-address 0.0.0.0 \
    --end-ip-address 255.255.255.255

sudo az keyvault set-policy --name $KEYVAULTNAME -g $RESOURCE_GROUP  --spn $SP_APPID --secret-permissions get list
echo "########################## "
echo -e "Creating service bus and queue for messaging"
echo "########################## "

#namespace is very similar to namespace in programming languages
# every identifier identifies a dist
#every namespace=> for a unique app
sudo az servicebus namespace create --resource-group $RESOURCE_GROUP --name $SERVICEBUSNAME --location $LOCATION
SBCONNSTRING=$(sudo az servicebus namespace authorization-rule keys list --resource-group $RESOURCE_GROUP --namespace-name $SERVICEBUSNAME --name RootManageSharedAccessKey --query primaryConnectionString -o tsv)
#queue=>list of messages=>MQ
#every queue has its own list of participants=> senders and receivers \
#queue=> group chat thread on whatsapp
sudo az servicebus queue create --resource-group $RESOURCE_GROUP --namespace-name $SERVICEBUSNAME --name $SERVICEBUSQUEUE
echo -e "azure.keyvault.client-id="$SP_APPID
echo -e "azure.keyvault.client-key="$SP_PASSWORD
echo -e "azure.keyvault.tenant-id="$SP_TENANT_ID
echo -e "azure.keyvault.uri=https://"$KEYVAULTNAME".vault.azure.net/"
echo -e "spring.jms.servicebus.connection-string="$SBCONNSTRING
egrep -lRZ "3243dffc-1cc3-42e6-89ad-9d7eeed1d23b" | xargs -0 -l sed -i -e s/"3243dffc-1cc3-42e6-89ad-9d7eeed1d23b"/$SP_APPID/g
egrep -lRZ "S~GBcRDBuddGAFUPlj1Oj7YwH03drJ.0Ld" | xargs -0 -l sed -i -e s/"S~GBcRDBuddGAFUPlj1Oj7YwH03drJ.0Ld"/$SP_PASSWORD/g
egrep -lRZ "https://contosokvakm.vault.azure.net/" | xargs -0 -l sed -i -e s+"https://contosokvakm.vault.azure.net/"+"https://"$KEYVAULTNAME".vault.azure.net/"+g
egrep -lRZ $SC_OLDCONNECTION | xargs -0 -l sed -i -e "s+$SC_OLDCONNECTION+$SBCONNSTRING+g"
egrep -lRZ $CLIENT_ORIG_IP | xargs -0 -l sed -i -e s/$CLIENT_ORIG_IP/$CLIENT_NEW_IP/g
egrep -lRZ $SERVER_ORIG_IP | xargs -0 -l sed -i -e s/$SERVER_ORIG_IP/$SERVER_NEW_IP/g
egrep -lRZ $POSTGRES_ORIG_USER | xargs -0 -l sed -i -e s/$POSTGRES_ORIG_USER/$/g
egrep -lRZ $POSTGRES_ORIG_PASSWD | xargs -0 -l sed -i -e s/$POSTGRES_ORIG_PASSWD/$POSTGRES_NEW_PASSWD/g
egrep -lRZ $POSTGRES_ORIG_DB | xargs -0 -l sed -i -e s/$POSTGRES_ORIG_DB/$POSTGRES_NEW_DB/g
egrep -lRZ $POSTGRES_USER_NAME_ORIG | xargs -0 -l sed -i -e s/$POSTGRES_USER_NAME_ORIG/$POSTGRES_USER_NAME/g
egrep -lRZ "anjnaq" | xargs -0 -l sed -i -e s/"anjnaq"/"anjnaq"$VERSION/g
cd stocks-master
mvn install
cd ..
cd stocks-react-master
npm clean install
cd ..
cd stocks-worker
mvn clean install
cd ..
cd ..
cd Step5
cd stocks-master
mvn install
cd ..
cd stocks-react-master
npm install
cd ..
cd stocks-worker
mvn install
cd ..
