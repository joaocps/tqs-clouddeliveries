import requests
import json

def decorator(method):
	if method.__name__ in [ '__init__', 'getDriver', 'getDrivers',  'getVeichleType', 'getVeichleTypes', 'getVeichle','getVeichles',  'authenticate', 'logout', 'getOrders']:
		return method
	def func(self, *args, **kwargs):
		result = method(self, *args, **kwargs)
		if result.status_code != 200:
			print("Argumentos Errados")
		else:
			print("Ordem efetuada com sucesso")
	return func

def for_all_methods(decorator):
    def decorate(cls):
        for attr in cls.__dict__: # there's propably a better way to do this
            if callable(getattr(cls, attr)):
                setattr(cls, attr, decorator(getattr(cls, attr)))
        return cls
    return decorate


@for_all_methods(decorator)
class CloudDeliveries():

	def __init__(self, base_url = 'http://192.168.160.6:8080/'):
		self.base_url = base_url

	def authenticate(self, email, password):
		url = self.base_url + "user/admin/login/"
		r = requests.post(url, json = {'email' : email, 'password' : password})
		if r.status_code == 200:
			return {'status' : r.status_code, 'token' : r.content}
		print("Argumentos inválidos")
		return {'status' : r.status_code, 'token' : "-1".encode()}

	def logout(self, email):
		url = self.base_url + "user/admin/logout/" + email
		r = requests.delete(url)
		if r.status_code == 200:
			return r.content
		print("Argumentos inválidos")

	def addDriver(self, contact):
		url = self.base_url + "driver/"
		r = requests.post(url, json = {'contact' : contact})
		return r

	def deleteDriver(self, idd):
		url = self.base_url + 'driver/' + str(idd) + '/';
		r = requests.delete(url)
		return r

	def updateDriver(self, idd, contact):
		url = self.base_url + 'driver/' + str(idd) + '/';
		return requests.put(url, json = {'contact' : contact})
		

	def getDriver(self, idd):
		url = self.base_url + 'driver/' + str(idd) + '/';
		return Driver(requests.get(url).content.decode())

	def getDrivers(self):
		url = self.base_url + 'driver/'
		r = requests.get(url).json()
		return [Driver(json.dumps(a)) for a in r]

	def addVeichleType(self, name, capacity):
		url = self.base_url + "typeveichle/"
		r = requests.post(url, json = {'name' : name, 'capacity' : capacity})
		return r

	def deleteVeichleType(self, idd):
		url = self.base_url + 'typeveichle/' + str(idd) + '/';
		r = requests.delete(url)
		return r

	def updateVeichleType(self, idd, name, capacity):
		url = self.base_url + 'typeveichle/' + str(idd) + '/';
		r = requests.post(url, json = {'name' : name, 'capacity' : capacity})
		return r
		

	def getVeichleType(self, idd):
		url = self.base_url + 'typeveichle/' + str(idd) + '/';
		r = requests.get(url)
		return 

	def getVeichleTypes(self):
		url = self.base_url + 'typeveichle/'
		r = requests.get(url).json()
		return [VeichleType(json.dumps(a)) for a in r]


	def addVeichle(self, id_type, id_driver):
		url = self.base_url + "veichle/"
		r = requests.post(url, json = {'id_veichle_type' : id_type, 'id_driver' : id_driver})
		return r

	def deleteVeichle(self, idd):
		url = self.base_url + 'veichle/' + str(idd) + '/';
		r = requests.delete(url)
		return r

	def updateVeichle(self, idd, id_type, id_driver):
		url = self.base_url + 'veichle/' + str(idd) + '/';
		return requests.put(url, json = {'id_veichle_type' : id_type, 'id_driver' : id_driver})
		

	def getVeichle(self, idd):
		url = self.base_url + 'veichle/' + str(idd) + '/';
		return Veichle(requests.get(url).content.decode())

	def getVeichles(self):
		url = self.base_url + 'veichle/'
		r = requests.get(url).json()
		return [Veichle(json.dumps(a)) for a in r]

	def getOrders(self, email):
		url = self.base_url + 'order/user/' + email
		r = requests.get(url)
		content = r.content.decode()
		return content

	def updateOrderState(self, idd, state):
		url = self.base_url + 'order/state/' + idd + '/' + state + '/'
		r = requests.put(url)
		return r






class Driver:
	def __init__(self, j):
		self.__dict__ = json.loads(j)	

	def toJSON(self):
		return "id : " + str(self.id) +  ", contact : " + str(self.contact) 

class VeichleType:
	def __init__(self, j):
		self.__dict__ = json.loads(j)


	def toJSON(self):
		return json.dumps(self, default = lambda o : o.__dict__, sort_keys = True, indent = 4)

class Veichle:
	def __init__(self, j):
		self.__dict__ = json.loads(j)


	def toJSON(self):
		return json.dumps(self, default = lambda o : o.__dict__, sort_keys = True, indent = 4)



		

