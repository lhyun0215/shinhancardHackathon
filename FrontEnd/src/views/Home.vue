<template>
  <div>
    <div class="form-group">
      <label for="writer">출발지 </label>
      <input
        type="text"
        class="form-control"
        id="writer"
        ref="writer"
        placeholder="출발지를 입력하세요"
        v-model="startLoc"
      />
    </div>
    <div class="form-group">
      <label for="title">목적지 </label>
      <input
        type="text"
        class="form-control"
        id="title"
        ref="title"
        placeholder="목적지를 입력하세요"
        v-model="endLoc"
      />
    </div>
    <div class="text-right">
      <button class="btn btn-primary" @click="trafastRoot">
        빠른길찾기
      </button>
      <button class="btn btn-primary" @click="traavoidtollRoot">
        무료도로 길찾기
      </button>
    </div>

    <div id="map" style="width:100%; height:800px;"></div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      nowLat: '',
      nowLng: 0,
      startLoc: '',
      endLoc: '',
      showModal: false,
      marker: null,
      map: null,
      isCTT: false,
      polyline: null,
      options: {
        strokeWeight: 6,
        strokeOpacity: 6,
        strokeColor: '#FF0000',
        fillOpacity: 6,
        visible: true
      },
      mapOptions: {
        lat: 37,
        lng: 127,
        zoom: 10,
        zoomControl: true,
        zoomControlOptions: { position: 'TOP_RIGHT' },
        mapTypeControl: true
      },
      initLayers: [
        'BACKGROUND',
        'BACKGROUND_DETAIL',
        'POI_KOREAN',
        'TRANSIT',
        'ENGLISH',
        'CHINESE',
        'JAPANESE'
      ],

      pathLine: [
        { lat: 37, lng: 127 },
        { lat: 38, lng: 129 }
      ]
    }
  },

  created: function() {
    let naverMap = document.createElement('script')
    naverMap.setAttribute('type', 'text/javascript')
    naverMap.setAttribute(
      'src',
      'https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId={{apikey}}'
    )
    document.head.appendChild(naverMap)
  },
  mounted() {
    setTimeout(() => {
      this.initMap()
    }, 100)
  },
  methods: {
    initMap() {
      const mapOptions = {
        // eslint-disable-next-line no-undef
        center: new naver.maps.LatLng(37.2762087, 127.0808982),
        zoom: 13
      }
      // eslint-disable-next-line no-undef
      new naver.maps.Map('map', mapOptions)
    },
    test() {
      console.log('hello')
    },
    button1_click() {},
    trafastRoot() {
      this.$http
        .get('trafast/map', {
          params: {
            start: this.startLoc,
            end: this.endLoc
          }
        })
        .then(res => {
          const raw = res.data

          let path = raw.nevi.route.trafast[0].path
          // eslint-disable-next-line no-unused-vars
          let markloc = raw.list
          // eslint-disable-next-line no-unused-vars
          let starts = raw.start
          let data = []

          path.forEach(i => {
            const path = {
              lat: i[1],
              lng: i[0]
            }
            data.push(path)
          })

          const mapOptions = {
            // eslint-disable-next-line no-undef
            center: new naver.maps.LatLng(starts[0], starts[1]),
            zoom: 13
          }
          // eslint-disable-next-line no-undef
          const map = new naver.maps.Map('map', mapOptions)

          // eslint-disable-next-line no-undef
          new naver.maps.Polyline({
            map: map,
            path: data,
            clickable: true,
            strokeColor: '#aa4747',
            strokeStyle: 'longdash',
            strokeWeight: 5
          })
          console.log(markloc)
          markloc.forEach(i => {
            // eslint-disable-next-line no-undef,no-unused-vars
            var marker = new naver.maps.Marker({
              // eslint-disable-next-line no-undef
              position: new naver.maps.LatLng(i.lat, i.lng),
              map: map
            })
            // eslint-disable-next-line no-unused-vars
            var contents = i.name
            var contentString = [
              '<div class="iw_inner">',
              '   <h3>' + i.name + '</h3>',
              '   <p>' + '가스주유소 여부 : ' + i.gasStatus + '<br />',
              'LPG주유소 여부 : ' + i.lpgStatus + '<br />',
              '전기주유소 여부 : ' + i.elecStatus + '<br />',
              '가스주유소 여부 : ' + i.gasStatus + '<br />',
              '화장실 여부 : ' + i.toiletStatus + '<br />',
              '매점 여부 : ' + i.storeStatus + '<br />',
              '음식점 여부 : ' + i.foodStatus + '<br />',
              // '기타편의시설 여부 : ' + i.etcStore + '<br />',
              // '휴게소대표음식명 : ' + i.repFood + '<br />',
              '   <p>' +
                '<table border="1">' +
                '<tr>' +
                '<td>신한카드 : </td>' +
                ' <td>' +
                i.cardCompany.shanCard +
                '</td>' +
                ' </tr>' +
                '</table>',
              '</div>'
            ].join('')

            // eslint-disable-next-line no-undef
            var infowindow = new naver.maps.InfoWindow({
              content: contentString
            })

            // eslint-disable-next-line no-undef,no-unused-vars
            naver.maps.Event.addListener(marker, 'click', function(e) {
              if (infowindow.getMap()) {
                infowindow.close()
              } else {
                // eslint-disable-next-line no-undef
                infowindow.open(map, marker)
              }
            })

            // eslint-disable-next-line no-undef
            infowindow.open(map, marker)
          })
        })
    },
    traavoidtollRoot() {
      this.$http
        .get('traavoidtoll/map', {
          params: {
            start: this.startLoc,
            end: this.endLoc
          }
        })
        .then(res => {
          const raw = res.data

          let path = raw.nevi.route.traavoidtoll[0].path
          // eslint-disable-next-line no-unused-vars
          let markloc = raw.list
          // eslint-disable-next-line no-unused-vars
          let starts = raw.start
          let data = []

          path.forEach(i => {
            const path = {
              lat: i[1],
              lng: i[0]
            }
            data.push(path)
          })

          const mapOptions = {
            // eslint-disable-next-line no-undef
            center: new naver.maps.LatLng(37.2762087, 127.0808982),
            zoom: 13
          }
          // eslint-disable-next-line no-undef
          const map = new naver.maps.Map('map', mapOptions)

          // eslint-disable-next-line no-undef
          new naver.maps.Polyline({
            map: map,
            path: data,
            clickable: true,
            strokeColor: '#aa4747',
            strokeStyle: 'longdash',
            strokeWeight: 5
          })
          console.log(markloc)
          markloc.forEach(i => {
            // eslint-disable-next-line no-undef,no-unused-vars
            var marker = new naver.maps.Marker({
              // eslint-disable-next-line no-undef
              position: new naver.maps.LatLng(i.lat, i.lng),
              map: map
            })

            var contentString = [
              '<div class="iw_inner">',
              '   <h3>' + i.plcNm + '</h3>',
              '   <p>' + '주소 : ' + i.addr + '<br />',
              '메뉴 & 가격 : ' + i.menuInfoCn + '<br />',
              '영업시간  : ' + i.bzTimeCn + '<br />',
              '   <p>',
              '</div>'
            ].join('')

            // eslint-disable-next-line no-undef
            var infowindow = new naver.maps.InfoWindow({
              content: contentString
            })

            // eslint-disable-next-line no-undef,no-unused-vars
            naver.maps.Event.addListener(marker, 'click', function(e) {
              if (infowindow.getMap()) {
                infowindow.close()
              } else {
                // eslint-disable-next-line no-undef
                infowindow.open(map, marker)
              }
            })

            // eslint-disable-next-line no-undef
            infowindow.open(map, marker)
          })
        })
    }
  }
}
</script>
<style scoped></style>
