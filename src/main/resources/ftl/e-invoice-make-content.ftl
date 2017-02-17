<REQUEST_FPKJXX class="REQUEST_FPKJXX ">
	<FPKJXX_FPTXX class="FPKJXX_FPTXX">
		<FPQQLSH>${FPQQLSH?if_exists}</FPQQLSH>
		<DSPTBM>${DSPTBM?if_exists}</DSPTBM>
		<NSRSBH>${NSRSBH?if_exists}</NSRSBH>
		<NSRMC>${NSRMC?if_exists}</NSRMC>
		<NSRDZDAH></NSRDZDAH>
		<SWJG_DM></SWJG_DM>
		<DKBZ>${DKBZ?if_exists}</DKBZ>
		<PYDM>000001</PYDM>
		<KPXM>${KPXM?if_exists}</KPXM>
		<BMB_BBH>${BMB_BBH?if_exists}</BMB_BBH>
		<XHF_NSRSBH>${XHF_NSRSBH?if_exists}</XHF_NSRSBH>
		<XHFMC>${XHFMC?if_exists}</XHFMC>
		<XHF_DZ>${XHF_DZ?if_exists}</XHF_DZ>
		<XHF_DH>${XHF_DH?if_exists}</XHF_DH>
		<XHF_YHZH>中国银行</XHF_YHZH>
		<GHFMC>${GHFMC?if_exists}</GHFMC>
		<GHF_NSRSBH>${GHF_NSRSBH?if_exists}</GHF_NSRSBH>
		<GHF_SF>${GHF_SF?if_exists}</GHF_SF>
		<GHF_DZ>${GHF_DZ?if_exists}</GHF_DZ>
		<GHF_GDDH>${GHF_GDDH?if_exists}</GHF_GDDH>
		<GHF_SJ>${GHF_SJ?if_exists}</GHF_SJ>
		<GHF_EMAIL>${GHF_EMAIL?if_exists}</GHF_EMAIL>
		<GHFQYLX>${GHFQYLX?if_exists}</GHFQYLX>
		<GHF_YHZH>${GHF_YHZH?if_exists}</GHF_YHZH>
		<HY_DM>${HY_DM?if_exists}</HY_DM>
		<HY_MC>${HY_MC?if_exists}</HY_MC>
		<KPY>${KPY?if_exists}</KPY>
		<SKY>${SKY?if_exists}</SKY>
		<FHR>${FHR?if_exists}</FHR>
		<KPRQ>${KPRQ?if_exists}</KPRQ>
		<KPLX>${KPLX?if_exists}</KPLX>
		<YFP_DM>${YFP_DM?if_exists}</YFP_DM>
		<YFP_HM>${YFP_HM?if_exists}</YFP_HM>
		<CZDM>${CZDM?if_exists}</CZDM>
		<QD_BZ>${QD_BZ?if_exists}</QD_BZ>
		<QDXMMC>${QDXMMC?if_exists}</QDXMMC>
		<CHYY>${CHYY?if_exists}</CHYY>
		<TSCHBZ>${TSCHBZ?if_exists}</TSCHBZ>
		<KPHJJE>${KPHJJE?if_exists}</KPHJJE>
		<HJBHSJE>${HJBHSJE?if_exists}</HJBHSJE>
		<HJSE>${HJSE?if_exists}</HJSE>
		<BZ>${BZ?if_exists}</BZ>
		<BYZD1>${BYZD1?if_exists}</BYZD1>
		<BYZD2>${BYZD2?if_exists}</BYZD2>
		<BYZD3>${BYZD3?if_exists}</BYZD3>
		<BYZD4>${BYZD4?if_exists}</BYZD4>
		<BYZD5>${BYZD5?if_exists}</BYZD5>
	</FPKJXX_FPTXX>
	<FPKJXX_XMXXS class="FPKJXX_XMXX;" size="${FPKJXX_XMXXS?size}">
  <#list FPKJXX_XMXXS as G>
		<FPKJXX_XMXX>
			<XMMC>${G.XMMC?if_exists}</XMMC>
			<XMDW>${G.XMDW?if_exists}</XMDW>
			<GGXH>${G.GGXH?if_exists}</GGXH>
			<XMSL>${G.XMSL?if_exists}</XMSL>
			<HSBZ>${G.HSBZ?if_exists}</HSBZ>
			<XMDJ>${G.XMDJ?if_exists}</XMDJ>
			<FPHXZ>${G.FPHXZ?if_exists}</FPHXZ>
			<SPBM>${G.SPBM?if_exists}</SPBM>
			<ZXBM>${G.ZXBM?if_exists}</ZXBM>
			<YHZCBS>${G.YHZCBS?if_exists}</YHZCBS>
			<LSLBS></LSLBS>
			<ZZSTSGL>${G.ZZSTSGL?if_exists}</ZZSTSGL>
			<XMJE>${G.XMJE?if_exists}</XMJE>
			<SL>${G.SL?if_exists}</SL>
			<SE>${G.SE?if_exists}</SE>
			<BYZD1></BYZD1>
			<BYZD2></BYZD2>
			<BYZD3></BYZD3>
			<BYZD4></BYZD4>
			<BYZD5></BYZD5>
		</FPKJXX_XMXX>
  </#list>
	</FPKJXX_XMXXS>
	<FPKJXX_DDXX class="FPKJXX_DDXX">
		<DDH>${DDH?if_exists}</DDH>
		<THDH></THDH>
		<DDDATE></DDDATE>
	</FPKJXX_DDXX>
	<FPKJXX_DDMXXXS class="FPKJXX_DDMXXX;" size="0"/>
	<FPKJXX_ZFXX class="FPKJXX_ZFXX"/>
	<FPKJXX_WLXX class="FPKJXX_WLXX"/>
</REQUEST_FPKJXX>