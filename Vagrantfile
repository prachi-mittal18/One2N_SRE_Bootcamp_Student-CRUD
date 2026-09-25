Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/jammy64"

  config.vm.hostname = "student-crud-prod"

  # Give the VM a bit of headroom: it'll run 2 Java processes + Postgres + Nginx
  config.vm.provider "virtualbox" do |vb|
    vb.memory = 2048
    vb.cpus = 2
  end

  # Laptop:8080 -> VM:8080. This is how Postman on your laptop
  # reaches Nginx running inside the VM.
  config.vm.network "forwarded_port", guest: 8080, host: 8080

  # Vagrant syncs the project folder into /vagrant inside the VM by default —
  # this line just makes that explicit.
  config.vm.synced_folder ".", "/vagrant"

  # Runs once, the first time the VM is created (or on `vagrant provision`)
  config.vm.provision "shell", path: "scripts/vagrant-provision.sh"
end