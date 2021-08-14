wget https://download.java.net/java/GA/jdk13.0.2/d4173c853231432d94f001e99d882ca7/8/GPL/openjdk-13.0.2_linux-x64_bin.tar.gz 
tar -xvzf openjdk-13.0.2_linux-x64_bin.tar.gz 
sudo mv jdk-13.0.2/ /opt
JAVA_HOME=/opt/jdk-13.0.2
PATH=$PATH:/$JAVA_HOME/bin
export PATH
echo "########################## "
echo -e  "Install Maven"
echo "######################### "
wget https://apache.osuosl.org/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.tar.gz
tar -xvzf apache-maven-3.8.1-bin.tar.gz
sudo mv apache-maven-3.8.1 /opt
M2_HOME=/opt/apache-maven-3.8.1
PATH=$PATH:/$M2_HOME/bin
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
sudo dnf install https://download.docker.com/linux/centos/7/x86_64/stable/Packages/containerd.io-1.2.6-3.3.el7.x86_64.rpm
sudo dnf install docker-ce
sudo systemctl enable --now docker
echo "########################## "
echo -e "Install Docker Compose"
echo "######################### "
curl -L "https://github.com/docker/compose/releases/download/1.23.2/docker-compose-Linux-x86_64" -o docker-compose
sudo mv docker-compose /usr/local/bin && sudo chmod +x /usr/local/bin/docker-compose
sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
sudo rpm --import https://packages.microsoft.com/keys/microsoft.asc
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
echo -e "COMPLETE SETUP"
