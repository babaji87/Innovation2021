echo "########################## "
echo -e  " Install Java "
echo "######################### "
sudo wget https://download.java.net/java/GA/jdk13.0.2/d4173c853231432d94f001e99d882ca7/8/GPL/openjdk-13.0.2_linux-x64_bin.tar.gz
sudo tar -xvzf openjdk-13.0.2_linux-x64_bin.tar.gz
sudo mv jdk-13.0.2/ /opt
JAVA_HOME=/opt/jdk-13.0.2
PATH=$PATH:$JAVA_HOME/bin
export PATH
echo "########################## "
echo -e  "Install Maven"
echo "######################### "
wget https://apache.osuosl.org/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.tar.gz
tar -xvzf apache-maven-3.8.1-bin.tar.gz
sudo mv apache-maven-3.8.1 /opt
M2_HOME=/opt/apache-maven-3.8.1
PATH=$PATH:$M2_HOME/bin
export PATH
echo "########################## "
echo -e "Install NodeJS"
echo "######################### "
sudo dnf install -y nodejs
echo "########################## "
echo -e "Install Docker"
echo "######################## "
sudo dnf config-manager --add-repo=https://download.docker.com/linux/centos/docker-ce.repo
sudo dnf install --nobest docker-ce
sudo dnf install https://download.docker.com/linux/centos/7/x86_64/stable/Packages/containerd.io-1.2.6-3.3.el7.x86_64.rpm
sudo dnf install docker-ce
sudo systemctl enable --now docker
echo "########################## "
echo -e "Install Docker Compose"
echo "######################### "
curl -L "https://github.com/docker/compose/releases/download/1.23.2/docker-compose-Linux-x86_64" -o docker-compose
sudo mv docker-compose /usr/local/bin && sudo chmod +x /usr/local/bin/docker-compose
sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
sudo rpm --import https://packages.microsoft.com/keys/microsoft.asc
echo "########################## "
echo -e "Install AZURE CLI"
echo "######################## "
echo -e "[azure-cli]
name=Azure CLI
baseurl=https://packages.microsoft.com/yumrepos/azure-cli
enabled=1
gpgcheck=1
gpgkey=https://packages.microsoft.com/keys/microsoft.asc" | sudo tee /etc/yum.repos.d/azure-cli.repo
sudo dnf install azure-cli
echo "############################"
echo    "Install Kubernetes CLI#"
echo "############################"
sudo chmod 777 /etc/yum.repos.d
cp kubernetes.repo /etc/yum.repos.d
sudo yum install -y kubectl
echo "########################## "
echo -e  " Build demo project"
echo "######################### "
export CLIENT_ORIG_IP="10.1.207.97"
export CLIENT_NEW_IP="10.1.206.97"
export SERVER_ORIG_IP="10.1.207.98"
export SERVER_NEW_IP="10.1.206.98"
export POSTGRES_ORIG_DB="anjnapostgres"
export POSTGRES_NEW_DB="anjna1postgres"
export POSTGRES_ORIG_USER="anjnak@anjnapostgres"
export POSTGRES_NEW_USER="anjna1k@anjnapostgres"
export POSTGRES_ORIG_PASSWD="Postgres1"
export POSTGRES_NEW_PASSWD="Postgres2"
cd Step3
egrep -lRZ $CLIENT_ORIG_IP | xargs -0 -l sed -i -e s/$CLIENT_ORIG_IP/$CLIENT_NEW_IP/g
egrep -lRZ $SERVER_ORIG_IP | xargs -0 -l sed -i -e s/$SERVER_ORIG_IP/$SERVER_NEW_IP/g
egrep -lRZ $POSTGRES_ORIG_USER | xargs -0 -l sed -i -e s/$POSTGRES_ORIG_USER/$POSTGRES_NEW_USER/g
egrep -lRZ $POSTGRES_ORIG_PASSWD | xargs -0 -l sed -i -e s/$POSTGRES_ORIG_PASSWD/$POSTGRES_NEW_PASSWD/g
egrep -lRZ $POSTGRES_ORIG_DB | xargs -0 -l sed -i -e s/$POSTGRES_ORIG_DB/$POSTGRES_NEW_DB/g
cd stocks-master
mvn install
cd ..
cd stocks-react-master
npm install
cd ..
cd stocks-worker
mvn install
cd ..
echo "############################"
echo -e "COMPLETE SETUP"

