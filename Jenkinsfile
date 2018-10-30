#!groovy

pipeline {
	agent any 

	options{
		skipDefaultCheckout(true)
	}

	stages {
		stage ("Checkout") {
			steps{
				checkout([$class: 'GitSCM',branches: [[name: '*/master']],doGenerateSubmoduleConfigurations: false,
				extensions: [],submoduleCfg: [],userRemoteConfigs: [[credentialsId: "cfceb732-7f76-48a7-9aab-8ea6a94f4d2d",
				url: "https://github.com/snmaddula/basic-ms-app.git"]]])

				load "jenkins-env.properties"
			}
		}

		stage("Build & Unit Tests") {
			steps{
				script{
					def mvnHome = tool 'M3';
					sh "${mvnHome}/bin/mvn ${build_with_unittest}"

				}
			}
		}

		stage("SonarQube") {
			steps{
				script{
					def scannerHome = tool 'sonarqubescanner';

					withSonarQubeEnv('Sonar Server') {
						sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=${sonar_projectKey} -Dsonar.projectName=${sonar_projectName} -Dsonar.sources=${sonar_sources}  -Dsonar.java.binaries=${sonar_java_maven_binaries} "

						sh "cat .scannerwork/report-task.txt"
						def props = readProperties  file: '.scannerwork/report-task.txt'
						echo "properties=${props}"
						def sonarServerUrl=props['serverUrl']
						def ceTaskUrl= props['ceTaskUrl']

						def ceTask

						timeout(time: 1, unit: 'MINUTES') {
							waitUntil {
								def response = httpRequest ceTaskUrl
								ceTask = readJSON text: response.content
								echo ceTask.toString()
								return "SUCCESS".equals(ceTask["task"]["status"])
							}
						}

						def response2 = httpRequest url : sonarServerUrl + "/api/qualitygates/project_status?analysisId=" + ceTask["task"]["analysisId"], authentication: 'sonar-quality-auth'
						def qualitygate =  readJSON text: response2.content
						echo qualitygate.toString()

						if ("ERROR".equals(qualitygate["projectStatus"]["status"])) {
							error  "Quality Gate failure"
						}

					}
				}
			}
		}

		stage("Deploy to Docker") {
			steps{
				script{
					def pom = readMavenPom file: 'pom.xml'
					def PACKAGE_VER = pom.version
					sh "git rev-parse --short HEAD > .git/commit-id"
					def SOURCE_VER = readFile('.git/commit-id').trim()
					SERVICE_TAG="${PACKAGE_VER}-${SOURCE_VER}"

					def docker = tool 'docker'

					withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: "${docker_credentials_id}", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
						sh "${docker}/docker build --network=host --no-cache -t ${service_image}:${SERVICE_TAG} ."
						sh "${docker}/docker tag  ${service_image}:${SERVICE_TAG} ${docker_registry}/${service_image}:${SERVICE_TAG}"
						sh "${docker}/docker push ${docker_registry}/${service_image}:${SERVICE_TAG}"
					}
				}
			}
		}


		stage("SourceClear") {
			steps{
				script{
					withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: "${dev_onprim_credentials_id}", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
					 	sh "echo \"sudo su && cd ${WORKSPACE} && /opt/srcclr/3.1.4/bin/srcclr --profile='MS Accelerator' scan .\" > srcclr_run.sh"
					 	sh "cat srcclr_run.sh | sshpass -p $PASSWORD ssh -o StrictHostKeyChecking=no $USERNAME@${dev_onprim_server}"  
					 }
				}
			}
		}


		stage("Clair") {
			steps{
				script{
					sh "~/clair-scanner_linux_amd64 --ip ${clair_scanner_ip} ${service_image}:${SERVICE_TAG}"
				}
			}
		}

		stage("HawkEye") {
			steps{
				script{
					sh "docker run --rm -v $WORKSPACE:/target hawkeyesec/scanner-cli -j hawkeye-results.json -f critical 2>&1 "
				}
			}
		}

		stage("dev_deploy") {
			steps{
				script{
					withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: "${dev_onprim_credentials_id}", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
						withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: "${docker_credentials_id}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS']]) {
							sh"rm -rf deploy_dev.sh && echo \"docker login ${docker_registry} -u $DOCKER_USER -p $DOCKER_PASS\" >> deploy_dev.sh"
							sh"echo \"docker stop ${service_image}\" >> deploy_dev.sh"
							sh"echo \"docker rm -f ${service_image}\" >> deploy_dev.sh"
							def DOCKER_ENV = ''
							for (String i : readFile('jenkins-env.properties').split("\r?\n")) {
								if(i.matches("docker_config_(.*)")){
									DOCKER_ENV = $DOCKER_ENV + ' -e ' + i.replace('docker_config_','')
								}
							}
							sh"set +x && echo \"docker run -d -t -i $DOCKER_ENV -p ${service_port}:${container_port} --name=${service_image} ${docker_registry}/${service_image}:${SERVICE_TAG}\" >> deploy_dev.sh"
							sh "cat deploy_dev.sh | sshpass -p $PASSWORD ssh -o StrictHostKeyChecking=no $USERNAME@${dev_onprim_server}"
							sh"rm -rf deploy_dev.sh"
						}
					}
				}
			}
		}

	}


}
