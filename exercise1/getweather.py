import os, pyowm

try:
	owm_key = os.environ['OPENWEATHER_API_KEY']
	city_name = os.environ['CITY_NAME']
except KeyError as e:
	print('Please specify environment variable: ' + str(e))
	exit()

try:
	owm = pyowm.OWM(owm_key)
	observation = owm.weather_at_place(city_name)
	weather = observation.get_weather()
except Exception as e:
	print('Failed to get weather info: ' + str(e))
	exit()

actual_weather = "source=%s, city=%s, description=%s, temp=%.1f, humidity=%d" % \
		('openweathermap',
		city_name,
		weather.get_detailed_status(),
		weather.get_temperature('celsius').get('temp'),
		weather.get_humidity())

print(actual_weather)
